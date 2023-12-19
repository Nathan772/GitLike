package fr.uge.gitclout.git;

import fr.uge.gitclout.Contribution.Contribution;
import fr.uge.gitclout.Documentation.Documentation;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GitManager {
  private Git git;
  private final String remoteURL;
  private final Path localPath;
  private final String projectName;
  private final Repository repository;
  /* it represents all the documents chosen by the user (all the file that exists and documentation files that exists) */
  private final ArrayList<Documentation> documentations;

  private static final String PATH = "target/tmp";

  /**
   *
   * @param remoteURL
   * it represents the url of the git project
   * @param localPath
   * it represents
   * @param documentationChosenByUser
   * it's the file that contains all the language and documentation (BUILD, CONFIG, ETC...) available.
   * @throws IOException
   */
  public GitManager(String remoteURL, String localPath, Path documentationChosenByUser) throws IOException {
    Objects.requireNonNull(remoteURL);
    Objects.requireNonNull(localPath);
    this.remoteURL = remoteURL;
    this.projectName = createProjectName(remoteURL);
    this.localPath = Path.of(localPath);
    if (new File(localPath).exists()) {
      git = Git.open(new File(this.localPath.toString()));
    }
    this.repository = new Repository(projectName, remoteURL, this.localPath.toString());
    this.documentations = Documentation.fromPathToListDocumentation(documentationChosenByUser);
  }

  public GitManager(String remoteURL) throws IOException {
    this(remoteURL, Path.of(PATH).resolve(createProjectName(remoteURL)).toString(), Path.of("FILES/langages_reconnus/langagesListe.txt"));
  }

  private static String createProjectName(String remoteURL) {
    if (remoteURL.endsWith(".git")) {
      return remoteURL.substring(remoteURL.lastIndexOf("/") + 1, remoteURL.lastIndexOf("."));
    }
    return remoteURL.substring(remoteURL.lastIndexOf("/") + 1);
  }

  public static GitManager fromRepository(Repository repository) throws IOException {
    return new GitManager(repository.getURL(), repository.getLocalPath(), Path.of("FILES/langages_reconnus/langagesListe.txt"));
  }

  public boolean isCloned() {
    return new File(localPath.toString()).exists();
  }

  public Repository cloneRepository() throws GitAPIException {

    var file = new File(PATH+"/"+projectName);
    //if the project had already been imported
    if(file.exists() && file.isDirectory()){
      //case when the project has already been import
        try {
            git = Git.open(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //if the project never had been imported
    else
      // clone the repo in a directory which has the same name as the repo
      // gives the path for the location of the directory

      git = Git.cloneRepository().setURI(remoteURL).setDirectory(file).call();
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
  /**
   * give the list of tags that are with the repo git.
   *
   * @return
   * the list of tags associated to the git repo.
   *
   */
  public List<Ref> retrieveTags() throws GitAPIException {
    return git.tagList().call();
  }


  public void close() {
    git.close();
  }
  /**
   * This method parse every file from the repo and give the user per line.
   * @param directoryForRepoStorage
   * exception for the use of method from retrieveEveryFileFromDepo.
   * @throws IOException
   * exception for the use of the method from parseOneFileForEachTag.
   */
  public List<String> retrieveEveryFileFromRepo(String directoryForRepoStorage) throws IOException {
    Path startDir = Paths.get(localPath.toString());
    List<Path> fileList = Files.walk(startDir).filter(Files::isRegularFile).toList();

    //var pattern = Pattern.compile(".*\\.com\\/(.*)");
    var files = fileList.stream().map(Path::toString).collect(Collectors.toList());
    //retrieve the part of the file name which is relevant

    return files.stream().map(x -> x.substring((directoryForRepoStorage).length() + 1)).toList();
  }

  /**
   *
   * This method parse every file from the repo and give the user per line.
   *
   * @throws GitAPIException
   * exception for the use of method from retrieveEveryFileFromDepo.
   * @throws IOException
   * exception for the use of the method from parseOneFileForEachTag.
   */

  public List<Contribution> parseEveryFileFromCurrentRepoAndTransformItIntoContribution() throws GitAPIException, IOException {
    //the final lists with all hte contributions
    var contributesList = new ArrayList<Contribution>();
    int compteur = 0;
    var files = retrieveEveryFileFromRepo(localPath.toString());
    for (var file : files) {
      //need to be deleted (à supprimer).
      if (compteur == 10) // stop after 30 files in order to not wait too much
        break;
      var listContributes = parseOneFileForEachTagWithContributors(Path.of(file));

      contributesList.addAll(listContributes);
      if (!listContributes.isEmpty())
        compteur++;
    }
    return contributesList;
  }
  /**
   * Fill an hashMap "user, number of line"  with the number of line by user using the result of a blame
   * @param hashUserLine
   * the hashMap you want to fill
   * @param blameResult
   * the result of the blame you made on a file.
   */
  public static void feedHashWithBlame(HashMap<Contributor, Integer> hashUserLine, BlameResult blameResult) {
    //retrieve the total of rows of the blame
    for (int i = 0; i < blameResult.getResultContents().size(); i++) {
      hashUserLine.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()),0,(oldValue, newValue) -> oldValue+1);
    }
  }

    /**
     *
     * @param contributions
     * the list that will contain the contributions made by the users.
     * @param hashUserLine
     * @param documentation
     * it represents the kind of documentation used by the user.
     */
  public void fromMapToListContribution(ArrayList<Contribution> contributions, HashMap<Contributor, Integer> hashUserLine, Documentation documentation, Ref tag) {
    Objects.requireNonNull(tag);
    Objects.requireNonNull(documentation);
    Objects.requireNonNull(hashUserLine);
    Objects.requireNonNull(contributions);
    //c'est tard, faites le plus tôt dans le programme
    //var documentationList = Documentation.fromPathToListDocumentation(filePath);
    // var repo = repository;
    //var language = Language.fromFileToLanguage(languages, filePath);
    //var file = new MyFile(filePath, language, repo);

    var tag1 = new Tag(tag.getName(), tag.getName(), new Date(), repository);
    hashUserLine.forEach((user, lines) ->
            contributions.add(new Contribution(new Contributor(user.name(), user.email()), tag1, documentation , 0, lines))
    );
  }

  /**
   *
   * this method enable to convert the data from a filePath to information about its kind of documentation
   * (example : its code type and its language C)
   * @param file
   * the filePath you want to use in order to know its kind.
   * @return
   * the type of documentation it represents( BUILD, C programming, Python programming, angular programming ...)
   */
  public Documentation fromFileToDocumentation(Path file){
    for(var document:documentations){
        var pattern = Pattern.compile(".*\\."+document.extension());
        var matcher = pattern.matcher(file.toString());
        if(matcher.find()){
          if(document.language().isPresent())
            //return new Documentation(document.language().orElseThrow(), document.fileType(),document.extension());
            return document;
          else
            return new Documentation(null, document.fileType(),document.extension());
        }
    }
    return Documentation.unknownFile();
  }

  /**
   *
   * This method blame every version (tag) of a given file and display the number of line by user.
   * @param filePath
   * the path for the path you want to blame
   * @throws GitAPIException
   * handle the use of blame
   * @return
   * a list with all the contribution
   */
  public List<Contribution> parseOneFileForEachTagWithContributors(Path filePath) throws GitAPIException {
    Objects.requireNonNull(filePath, "the file you're parsing cannot be null");
    //retrieve the document
    var document = fromFileToDocumentation(filePath);
    HashMap<Contributor, Integer> hashUserLine = new HashMap<Contributor, Integer>();
    var listContributes = new ArrayList<Contribution>();
    int compteur = 0;
    for (var tag : retrieveTags()) {
      var blameResult = git.blame().setFilePath(filePath.toString()).setStartCommit(tag.getObjectId()).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
      if (blameResult != null) {
        compteur++;
        feedHashWithBlame(hashUserLine, blameResult);
        fromMapToListContribution(listContributes, hashUserLine, document, tag);
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
      /*"https://github.com/vuejs/core"
      "https://github.com/facebookresearch/llama"*/
      var repositoryPath = "https://github.com/vuejs/core";
      var handler2 = new GitManager(repositoryPath);
      handler2.cloneRepository();
      var resContribution = handler2.parseEveryFileFromCurrentRepoAndTransformItIntoContribution();
            for(var contribution:resContribution){
                System.out.println(contribution);
            }
    } catch (GitAPIException | IOException e) {
      throw new RuntimeException(e);
    }

  }
}