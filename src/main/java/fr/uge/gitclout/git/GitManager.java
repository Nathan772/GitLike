package fr.uge.gitclout.git;

import Language.Language;
import fr.uge.gitclout.model.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GitManager {
  private Git git;
  private final Path remoteURL;
  private final Path localPath;
  private final String projectName;
  private final Repository repository;

  private static final String PATH = "target/tmp";

  public GitManager(String remoteURL, String localPath) throws IOException {
    Objects.requireNonNull(remoteURL);
    Objects.requireNonNull(localPath);
    this.remoteURL = Path.of(remoteURL);
    this.projectName = createProjectName(remoteURL);
    this.localPath = Path.of(localPath);
    if (new File(localPath).exists()) {
      git = Git.open(new File(this.localPath.toString()));
    }
    this.repository = new Repository(projectName, remoteURL, this.localPath.toString());
  }

  public GitManager(String remoteURL) throws IOException {
    this(remoteURL, Path.of(PATH).resolve(createProjectName(remoteURL)).toString());
  }

  private static String createProjectName(String remoteURL) {
    if (remoteURL.endsWith(".git")) {
      return remoteURL.substring(remoteURL.lastIndexOf("/") + 1, remoteURL.lastIndexOf("."));
    }
    return remoteURL.substring(remoteURL.lastIndexOf("/") + 1);
  }

  public static GitManager fromRepository(Repository repository) throws IOException {
    return new GitManager(repository.getURL(), repository.getLocalPath());
  }

  public boolean isCloned() {
    return new File(localPath.toString()).exists();
  }

  public Repository cloneRepository() throws GitAPIException {
    git = Git.cloneRepository()
            .setURI(remoteURL.toString())
            .setDirectory(new File(localPath.toString()))
            .call();
    return repository;
  }

  private Date getTagDate(Ref ref) throws IOException {
    Objects.requireNonNull(ref);
    return git.getRepository().parseCommit(ref.getObjectId()).getAuthorIdent().getWhen();
  }

  public List<Tag> getTags() throws GitAPIException {
    return git.tagList().call().stream().map(ref -> {
      try {
        var name = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
        return new Tag(name, ref.getName(), getTagDate(ref), repository);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).toList();
  }

  public List<Ref> retrieveTags() throws GitAPIException {
    //System.out.println("On a retrouvé les tags "+git.tagList().call());
    return git.tagList().call();
  }


  public void close() {
    git.close();
  }

  public List<String> retrieveEveryFileFromRepo(String directoryForRepoStorage) throws IOException {
    Path startDir = Paths.get(localPath.toString());
    List<Path> fileList = Files.walk(startDir).filter(Files::isRegularFile).toList();

    //var pattern = Pattern.compile(".*\\.com\\/(.*)");
    var files = fileList.stream().map(Path::toString).collect(Collectors.toList());
    //retrieve the part of the file name which is relevant

    return files.stream().map(x -> x.substring((directoryForRepoStorage).length() + 1)).toList();
  }

  public List<Contributes> parseEveryFileFromCurrentRepoAndTransformItIntoContribution() throws GitAPIException, IOException {
    //the final lists with all hte contributions
    var contributesList = new ArrayList<Contributes>();
    int compteur = 0;
    var files = retrieveEveryFileFromRepo(localPath.toString());
    for (var file : files) {
      if (compteur == 10) // stop after 30 files in order to not wait too much
        break;
      var listContributes = parseOneFileForEachTagWithContributors(file);

      contributesList.addAll(listContributes);
      if (!listContributes.isEmpty())
        compteur++;
    }
    return contributesList;
  }

  public static void feedHashWithBlame(HashMap<Contributor, Integer> hashUserLine, BlameResult blameResult) {
    //retrieve the total of rows of the blame
    for (int i = 0; i < blameResult.getResultContents().size(); i++) {
      //blameResultContents().
      //create a tuple for this user
      hashUserLine.computeIfAbsent(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), (k) -> 1);
      //increase by one the number of line for this user
      hashUserLine.computeIfPresent(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), (k, v) -> v + 1);
    }
  }

  public void fromMapToListContribution(ArrayList<Contributes> contributes, HashMap<Contributor, Integer> hashUserLine, String filePath, Ref tag) {
    Objects.requireNonNull(tag);
    Objects.requireNonNull(filePath);
    Objects.requireNonNull(hashUserLine);
    Objects.requireNonNull(contributes);
    var languages = Language.initLanguages();
    var repo = repository;
    var language = Language.fromFileToLanguage(languages, filePath);
    var file = new MyFile(filePath, language, repo);
    var tag1 = new Tag(tag.getName(), tag.getName(), new Date(), repo);
    hashUserLine.forEach((user, lines) ->
            contributes.add(new Contributes(new Contributor(user.name(), user.email()), tag1, file, 0, lines))
    );
  }

  public List<Contributes> parseOneFileForEachTagWithContributors(String filePath) throws GitAPIException {
    Objects.requireNonNull(filePath, "the file you're parsing cannot be null");
    HashMap<Contributor, Integer> hashUserLine = new HashMap<Contributor, Integer>();
    var listContributes = new ArrayList<Contributes>();
    int compteur = 0;
    for (var tag : retrieveTags()) {
      //System.out.println("le tag : "+tag.getName()+" a été trouvé");
      var blameResult = git.blame().setFilePath(filePath).setStartCommit(tag.getObjectId()).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
      if (blameResult != null) {
        compteur++;
        feedHashWithBlame(hashUserLine, blameResult);
        fromMapToListContribution(listContributes, hashUserLine, filePath, tag);
      }

      //test ici, à supprimer plus tard
      if (compteur == 1) {
        if (blameResult != null) {
          //System.out.println("" + blameResult.getResultContents());
      }}
    }
    return listContributes;
  }


  public static void main(String[] args) throws IOException, GitAPIException {
    /*var git = new GitManager("https://gitlab.ow2.org/asm/asm.git");
    //git.cloneRepository();
    System.out.println(git.getTags());
    git.close();*/

    try {
      var repositoryPath = "https://github.com/facebookresearch/llama";
      var handler2 = new GitManager(repositoryPath);
      //handler2.cloneRepository();
      var resContribution = handler2.parseEveryFileFromCurrentRepoAndTransformItIntoContribution();
            for(var contribution:resContribution){
                System.out.println(contribution);
            }
    } catch (GitAPIException | IOException e) {
      throw new RuntimeException(e);
    }

  }
}