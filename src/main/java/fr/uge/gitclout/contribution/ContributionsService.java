package fr.uge.gitclout.contribution;

import fr.uge.gitclout.app.json.JSONData;
import fr.uge.gitclout.app.json.JSONResponse;
import fr.uge.gitclout.contribution.db.Contribution;
import fr.uge.gitclout.contribution.db.ContributionDetail;
import fr.uge.gitclout.contribution.db.ContributionDetailRepository;
import fr.uge.gitclout.contribution.db.ContributionRepository;
import fr.uge.gitclout.languages.SupportedLanguages;
import fr.uge.gitclout.repository.db.RepositoryRepository;
import fr.uge.gitclout.tag.TagManager;
import fr.uge.gitclout.tag.db.Tag;
import fr.uge.gitclout.tag.db.TagRepository;
import io.micronaut.http.sse.Event;
import jakarta.transaction.Transactional;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.TreeWalk;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.net.URISyntaxException;
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
  private final List<Callable<Optional<String>>> callables = new ArrayList<>();
  private final SupportedLanguages supportedLanguages = new SupportedLanguages();

  private final Object lock = new Object();

  private final Map<String, AtomicInteger> fileCounters = new HashMap<>();
  private final Map<String, Integer> totalFileCounters = new HashMap<>();

  private final TagRepository tagRepository;
  private final RepositoryRepository repositoryRepository;
  private final ContributionRepository contributionRepository;
  private final ContributionDetailRepository contributionDetailRepository;

  public ContributionsService(TagRepository tagRepository, RepositoryRepository repositoryRepository, ContributionRepository contributionRepository, ContributionDetailRepository contributionDetailRepository) {
    Objects.requireNonNull(tagRepository);
    Objects.requireNonNull(repositoryRepository);
    Objects.requireNonNull(contributionRepository);
    Objects.requireNonNull(contributionDetailRepository);
    this.tagRepository = tagRepository;
    this.repositoryRepository = repositoryRepository;
    this.contributionRepository = contributionRepository;
    this.contributionDetailRepository = contributionDetailRepository;
  }

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

  private Set<Integer> findCommentLines(List<String> fileContentLines, Pattern commentPattern) {
    String fileContent = String.join("\n", fileContentLines);
    Matcher matcher = commentPattern.matcher(fileContent);
    Set<Integer> commentLineNumbers = new HashSet<>();
    while (matcher.find()) {
      int startLine = countLines(fileContent.substring(0, matcher.start())) + 1;
      int endLine = countLines(fileContent.substring(0, matcher.end())) + 1;
      for (int i = startLine; i <= endLine; i++) {
        commentLineNumbers.add(i - 1);
      }
    }
    return commentLineNumbers;
  }

  private int countLines(String str) {
    return (int) str.chars().filter(ch -> ch == '\n').count();
  }

  private BlameResult getBlameResult(Repository repository, String path, ObjectId commitId) {
    try {
      return new BlameCommand(repository)
              .setFilePath(path)
              .setStartCommit(commitId)
              .call();
    } catch (GitAPIException e) {
      throw new RuntimeException(e);
    }
  }

  private Set<Integer> getCommentedLines(BlameResult blameResult, String path) {
    Pattern regexComment = supportedLanguages.getCommentRegex(path);
    if (regexComment == null) {
      return new HashSet<>();
    }
    List<String> fileContentLines = IntStream.range(0, blameResult.getResultContents().size())
            .mapToObj(blameResult.getResultContents()::getString)
            .toList();
    return findCommentLines(fileContentLines, regexComment);
  }


  private void analyzeLines(BlameResult blameResult, String path, String tagName, Set<Integer> commentLineNumbers, ContributionType contributionType) {
    for (int i = 0; i < blameResult.getResultContents().size(); i++) {
      synchronized (lock) {
        RevCommit lineCommit = blameResult.getSourceCommit(i);
        String author = lineCommit.getAuthorIdent().getName() + " <" + lineCommit.getAuthorIdent().getEmailAddress() + ">";
        Contributions tagContributions = contributions.computeIfAbsent(tagName, k -> new Contributions());
        String language = supportedLanguages.getLanguage(path);
        boolean isComment = commentLineNumbers.contains(i);
        tagContributions.addAuthorContribution(author, contributionType, language, 1, isComment ? 1 : 0);

        if (contributionType == ContributionType.RESOURCE) {
          break;
        }
      }
    }
  }

  private void addBlameToCallables(Repository repository, String path, ObjectId commitId, String tag) {
    callables.add(() -> {
      var tagName = tag.replace("refs/tags/", "");
      BlameResult blameResult = getBlameResult(repository, path, commitId);
      Set<Integer> commentLineNumbers = getCommentedLines(blameResult, path);
      ContributionType contributionType = supportedLanguages.getType(path);
      analyzeLines(blameResult, path, tagName, commentLineNumbers, contributionType);

      fileCounters.putIfAbsent(tag, new AtomicInteger(0));
      fileCounters.get(tag).incrementAndGet();
      if (fileCounters.get(tag).get() == totalFileCounters.get(tag)) {
        return Optional.of(tagName);
      }
      return Optional.empty();
    });
  }

  private void walkTree(RevCommit commit, Repository repository, ObjectId commitId, String tag) {
    try (var treeWalk = new TreeWalk(repository)) {
      treeWalk.addTree(commit.getTree());
      treeWalk.setRecursive(true);
      for (boolean next = treeWalk.next(); next; next = treeWalk.next()) {
        String path = treeWalk.getPathString();
        if (!supportedLanguages.isSupported(path)) {
          continue;
        }
        totalFileCounters.putIfAbsent(tag, 0);
        totalFileCounters.computeIfPresent(tag, (k, v) -> v + 1);
        addBlameToCallables(repository, path, commitId, tag);

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Callable<Optional<String>>> getContributionsCallables(List<Ref> tags, Repository repository) {
    for (Ref tag : tags) {
      ObjectId commitId = getCommitId(tag);
      RevCommit commit = getCommit(commitId, repository);
      walkTree(commit, repository, commitId, tag.getName());
    }
    return callables;
  }

  private Map<String, List<AuthorContributions>> authorContributionsFromDB(List<Tag> analyzedTags) {
    Map<String, List<AuthorContributions>> authorContributions = new HashMap<>();
    for (var analyzedTag : analyzedTags) {
      var contributions = contributionRepository.findByTagAndRepository(analyzedTag, analyzedTag.getRepository());
      processContributionsForTag(authorContributions, analyzedTag, contributions);
    }
    return authorContributions;
  }

  private void processContributionsForTag(Map<String, List<AuthorContributions>> authorContributions, Tag analyzedTag, List<Contribution> contributions) {
    for (var contribution : contributions) {
      var contributionDetails = contributionDetailRepository.findByContributionId(contribution.getId());
      if (contributionDetails.isEmpty()) continue;

      var authorContribution = getOrCreateAuthorContribution(authorContributions, analyzedTag.getTagName(), contribution.getAuthor());

      authorContribution.addContribution(
              getContributionType(contribution),
              buildContributionTypeDetails(contribution, contributionDetails)
      );
    }
  }

  private AuthorContributions getOrCreateAuthorContribution(Map<String, List<AuthorContributions>> authorContributions, String tagName, String author) {
    return authorContributions.computeIfAbsent(tagName, k -> new ArrayList<>())
            .stream()
            .filter(c -> c.getAuthor().equals(author))
            .findFirst()
            .orElseGet(() -> createAndAddNewAuthorContribution(authorContributions, tagName, author));
  }

  private AuthorContributions createAndAddNewAuthorContribution(Map<String, List<AuthorContributions>> authorContributions, String tagName, String author) {
    AuthorContributions newContributions = new AuthorContributions(author);
    authorContributions.get(tagName).add(newContributions);
    return newContributions;
  }

  private ContributionTypeDetails buildContributionTypeDetails(Contribution contribution, List<ContributionDetail> contributionDetails) {
    Map<String, LanguageCount> details = new HashMap<>();
    for (var detail : contributionDetails) {
      details.putIfAbsent(detail.getLanguage(), new LanguageCount(detail.getLanguage(), detail.getCount(), detail.getCommentCount()));
    }
    return new ContributionTypeDetails(contribution.getTotal(), contribution.getTotalComment(), details);
  }


  private List<Ref> removeAnalyzedTagsFromTags(List<Tag> analyzedTags, List<Ref> tags) {
    for (var analyzedTag : analyzedTags) {
      tags.removeIf(tag -> tag.getName().equals(analyzedTag.getRefTagName()));
    }
    return tags;
  }

  @Transactional
  private void saveAll(Repository repository, List<Ref> tags, Map<String, Contributions> sentContributions) {
    var repo = saveRepository(repository);

    for (var tag : tags) {
      var tagDB = saveTag(repo, tag);

      var contributions = sentContributions.get(tag.getName().replace("refs/tags/", ""));
      if (contributions != null) {
        saveContributions(contributions, tagDB);
      }
    }
  }

  private fr.uge.gitclout.repository.db.Repository saveRepository(Repository repository) {
    String repoURL = repository.getConfig().getString("remote", "origin", "url");
    String name;
    try {
      name = new URIish(repoURL).getHumanishName();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    String localPath = repository.getDirectory().getAbsolutePath();

    var repo = new fr.uge.gitclout.repository.db.Repository(repoURL, name, localPath);
    repositoryRepository.findByRepositoryURL(repoURL).ifPresentOrElse(
            existingRepo -> System.out.println("Repository already exists: " + repoURL),
            () -> repositoryRepository.save(repo)
    );
    return repo;
  }

  private Tag saveTag(fr.uge.gitclout.repository.db.Repository repo, Ref tagRef) {
    var tagName = tagRef.getName().replace("refs/tags/", "");
    var tagDB = new Tag(tagName, tagRef.getName(), repo);
    tagRepository.findByTagNameAndRepositoryRepositoryURL(tagName, repo.getRepositoryURL()).ifPresentOrElse(
            existingTag -> System.out.println("Tag already exists: " + tagName),
            () -> tagRepository.save(tagDB)
    );
    return tagDB;
  }

  private void saveContributions(Contributions contributions, Tag tagDB) {
    for (var authorContribution : contributions.getContributions()) {
      var author = authorContribution.getAuthor();
      authorContribution.getContributionsByType().forEach((contributionType, details) -> {
        var contributionDB = saveContribution(author, contributionType, details, tagDB);
        saveContributionDetails(details, contributionDB);
      });
    }
  }

  private Contribution saveContribution(String author, ContributionType contributionType, ContributionTypeDetails details, Tag tagDB) {
    var contributionDB = new Contribution(author, contributionType.toString(), details.getTotal(),
            details.getTotalComment(), tagDB.getRepository(), tagDB
    );

    try {
      contributionRepository.save(contributionDB);
    } catch (Exception e) {
      throw new RuntimeException("Error saving contribution: " + e.getMessage(), e);
    }

    return contributionDB;
  }

  private void saveContributionDetails(ContributionTypeDetails details, Contribution contributionDB) {
    for (var entry : details.getDetails().entrySet()) {
      var language = entry.getKey();
      var languageCount = entry.getValue();

      var contributionDetailDB = new ContributionDetail(language, languageCount.getCount(), languageCount.getCommentCount(), contributionDB);

      try {
        contributionDetailRepository.save(contributionDetailDB);
      } catch (Exception e) {
        throw new RuntimeException("Error saving contribution detail: " + e.getMessage(), e);
      }
    }
  }

  private void emitEventsForAnalyzedTags(List<Tag> analyzedTags, Map<String, List<AuthorContributions>> authorContributions, Sinks.Many<Event<JSONResponse>> sink) {
    for (var analyzedTag : analyzedTags) {
      Event<JSONResponse> event = Event.of(new JSONResponse(
              "Retrieved information for tag '" + analyzedTag.getRefTagName() + "'.", "success",
              new JSONData.JSONContributions(analyzedTag.getTagName(), authorContributions.get(analyzedTag.getTagName()))
      ));
      sink.tryEmitNext(event);
    }
  }

  private void processNewTags(Repository repository, List<Ref> tags, Sinks.Many<Event<JSONResponse>> sink) {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    AtomicInteger count = new AtomicInteger(tags.size());
    var sentContributions = new HashMap<String, Contributions>();

    for (Callable<Optional<String>> callable : getContributionsCallables(tags, repository)) {
      CompletableFuture.runAsync(() -> {
        try {
          var tag = callable.call();
          if (tag.isPresent() && contributions.containsKey(tag.get())) {
            var tagName = tag.get();
            synchronized (contributions) {
              Event<JSONResponse> event = Event.of(new JSONResponse(
                      "Retrieved information for tag '" + tag + "'.", "success",
                      new JSONData.JSONContributions(tagName, contributions.get(tagName).getContributions())
              ));
              sentContributions.put(tagName, contributions.get(tagName));
              contributions.remove(tagName);
              count.getAndDecrement();
              sink.tryEmitNext(event);
            }
          }
        } catch (Exception e) {
          sink.tryEmitError(e);
        } finally {

          if (count.get() == 0) {
            sink.tryEmitComplete();
            saveAll(repository, tags, sentContributions);
          }
        }
      }, executorService);
    }
  }


  public Flux<Event<JSONResponse>> getContributions(Repository repository) throws GitAPIException, IOException {
    var gitTagManager = new TagManager(repository);
    var analyzedTags = tagRepository.findByRepositoryRepositoryURL(repository.getConfig().getString("remote", "origin", "url"));
    var authorContributions = authorContributionsFromDB(analyzedTags);
    var tags = removeAnalyzedTagsFromTags(analyzedTags, gitTagManager.getTags());

    Sinks.Many<Event<JSONResponse>> sink = Sinks.many().multicast().onBackpressureBuffer();
    emitEventsForAnalyzedTags(analyzedTags, authorContributions, sink);

    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    if (!tags.isEmpty()) {
      processNewTags(repository, tags, sink);
    } else {
      sink.tryEmitComplete();
    }

    return sink.asFlux().doFinally(signalType -> executorService.shutdown());
  }

  private static ContributionType getContributionType(Contribution contribution) {
    var type = contribution.getContributionType();
    ContributionType contributionType = ContributionType.OTHER;

    switch (type) {
      case "CODE" -> contributionType = ContributionType.CODE;
      case "RESOURCE" -> contributionType = ContributionType.RESOURCE;
      case "DOCUMENTATION" -> contributionType = ContributionType.DOCUMENTATION;
      case "CONFIG" -> contributionType = ContributionType.CONFIG;
      case "BUILD" -> contributionType = ContributionType.BUILD;
    }
    return contributionType;
  }
}
