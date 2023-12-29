package fr.uge.gitclout.contribution;

import fr.uge.gitclout.app.json.JSONData;
import fr.uge.gitclout.app.json.JSONResponse;
import fr.uge.gitclout.languages.SupportedLanguages;
import fr.uge.gitclout.tag.GitTagManager;
import io.micronaut.http.sse.Event;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ContributionsService {
  private final Map<String, Contributions> contributions = new HashMap<>();
  private final List<Callable<String>> callables = new ArrayList<>(); // TODO: return a Optional<String> instead of null
  private final SupportedLanguages supportedLanguages = new SupportedLanguages();

  private final Object lock = new Object();

  private final Map<String, AtomicInteger> fileCounters = new HashMap<>();
  private final Map<String, Integer> totalFileCounters = new HashMap<>();


  //private final Map<String, Map<String, Map<String, Integer>>> contributions = new HashMap<>();

  private ObjectId getCommitId(Ref tag) {
    ObjectId commitId = tag.getPeeledObjectId();
    if (commitId == null) {
      commitId = tag.getObjectId();
    }
    return commitId;
  }

  private RevCommit getCommit(ObjectId commitId, Repository repository) {
    try (var revWalk = new RevWalk(repository)) {
      return revWalk.parseCommit(commitId);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getFileExtension(String fileName) {
    int lastIndexOfDot = fileName.lastIndexOf(".");
    if (lastIndexOfDot > 0) {
      return fileName.substring(lastIndexOfDot);
    }
    return "";
  }

  public Set<Integer> findCommentLines(List<String> fileContentLines, Pattern commentPattern) {
    String fileContent = String.join("\n", fileContentLines);

    Matcher matcher = commentPattern.matcher(fileContent);
    Set<Integer> commentLineNumbers = new HashSet<>();
    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();

      int startLine = countLines(fileContent.substring(0, start)) + 1;
      int endLine = countLines(fileContent.substring(0, end)) + 1;

      for (int i = startLine; i <= endLine; i++) {
        commentLineNumbers.add(i - 1);
      }
    }
    return commentLineNumbers;
  }

  private int countLines(String str) {
    return (int) str.chars().filter(ch -> ch == '\n').count();
  }

  private void walkTree(RevCommit commit, Repository repository, ObjectId commitId, String tag) {
    try (var treeWalk = new TreeWalk(repository)) {
      treeWalk.addTree(commit.getTree());
      treeWalk.setRecursive(true);
      for (boolean next = treeWalk.next(); next; next = treeWalk.next()) {
        String path = treeWalk.getPathString();
        if (!supportedLanguages.isSupported(getFileExtension(path))) {
          continue;
        }
        totalFileCounters.computeIfAbsent(tag, k -> 0);
        totalFileCounters.computeIfPresent(tag, (k, v) -> v + 1);
        callables.add(() -> {
          var tagName = tag.replace("refs/tags/", "");
          try {

            BlameResult blameResult = new BlameCommand(repository)
                    .setFilePath(path)
                    .setStartCommit(commitId)
                    .call();

            Pattern regexComment = supportedLanguages.getCommentRegex(getFileExtension(path));
            Set<Integer> commentLineNumbers = new HashSet<>();
            if (regexComment != null) {
              List<String> fileContentLines = IntStream.range(0, blameResult.getResultContents().size())
                      .mapToObj(blameResult.getResultContents()::getString)
                      .toList();
              commentLineNumbers = findCommentLines(fileContentLines, regexComment);
            }


            String fileExtension = getFileExtension(path);
            ContributionType contributionType = supportedLanguages.getType(fileExtension);

            for (int i = 0; i < blameResult.getResultContents().size(); i++) {
              synchronized (lock) {
                RevCommit lineCommit = blameResult.getSourceCommit(i);
                String author = lineCommit.getAuthorIdent().getName() + " <" + lineCommit.getAuthorIdent().getEmailAddress() + ">";

                Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
                String language = supportedLanguages.getLanguage(fileExtension);

                boolean isComment = commentLineNumbers.contains(i);

                tagContributions.addAuthorContribution(
                        author,
                        contributionType,
                        language,
                        1,
                        isComment ? 1 : 0
                );

                if (contributionType == ContributionType.RESOURCE) {
                  break;
                }
              }
            }
          } catch (GitAPIException e) {
            throw new RuntimeException(e);
          }

          fileCounters.putIfAbsent(tag, new AtomicInteger(0));
          fileCounters.get(tag).incrementAndGet();
          if (fileCounters.get(tag).get() == totalFileCounters.get(tag)) {
            return tagName;
          }
          return null;
        });

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Callable<String>> getContributionsCallables(List<Ref> tags, Repository repository) {
    for (Ref tag : tags) {
      ObjectId commitId = getCommitId(tag);
      RevCommit commit = getCommit(commitId, repository);
      walkTree(commit, repository, commitId, tag.getName());
    }
    return callables;
  }


  public Flux<Event<JSONResponse>> getContributions(Repository repository) throws GitAPIException, IOException {
    var gitTagManager = new GitTagManager(repository);
    var tags = gitTagManager.getTags();
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    Sinks.Many<Event<JSONResponse>> sink = Sinks.many().multicast().onBackpressureBuffer();

    for (Callable<String> callable : getContributionsCallables(tags, repository)) {
      CompletableFuture.runAsync(() -> {
        try {
          String tag = callable.call();
          if (tag != null && contributions.containsKey(tag)) {
            synchronized (contributions) {
              Event<JSONResponse> event = Event.of(new JSONResponse(
                      "Retrieved information for tag '" + tag + "'.", "success",
                      new JSONData.JSONContributions(tag, contributions.get(tag).getContributions())
              ));
              contributions.remove(tag);
              tags.removeIf(ref -> ref.getName().equals("refs/tags/" + tag));
              sink.tryEmitNext(event);
            }
          }
        } catch (Exception e) {
          sink.tryEmitError(e);
        } finally {
          if (tags.isEmpty()) {
            sink.tryEmitComplete();
          }
        }
      }, executorService);
    }

    return sink.asFlux().doFinally(signalType -> executorService.shutdown());
  }


//  public Flux<Event<JSONResponse>> getContributions(Repository repository) throws GitAPIException, IOException, InterruptedException {
//    var gitTagManager = new GitTagManager(repository);
//    var tags = gitTagManager.getTags();
//
//    ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//    var callables = getContributionsCallables(tags, repository);
//
//    var futures = executorService.invokeAll(callables);
//
//    var tagsName = tags.stream().map(ref -> ref.getName().replace("refs/tags/", "")).toList();
//
//    return Flux.generate(() -> 0, (i, emitter) -> {
//      if (i < tagsName.size()) {
//        for (var future : futures) {
//          switch (future.state()) {
//            case RUNNING -> throw new RuntimeException("Future should not be running");
//            case SUCCESS -> {
//              try {
//                var tag = future.resultNow();
//                System.out.println("tag: " + tag);
//                if (tag != null && contributions.containsKey(tag)) {
//                  emitter.next(
//                          Event.of(
//                                  new JSONResponse(
//                                          "Retrieved information for tag '" + tag + "'.", "success",
//                                          new JSONData.JSONContributions(tag, contributions.get(tag).getContributions())
//                                  )
//                          )
//                  );
//                  futures.remove(future);
//                  contributions.remove(tag);
//                  return ++i;
//                }
//              } catch (Exception e) {
//                throw new RuntimeException(e);
//              }
//            }
//            case FAILED -> System.out.println(future.exceptionNow());
//            case CANCELLED -> System.out.println("cancelled");
//          }
//        }
//      } else {
//        executorService.shutdown();
//        emitter.complete();
//      }
//      return i;
//    });
//
//  }

}
