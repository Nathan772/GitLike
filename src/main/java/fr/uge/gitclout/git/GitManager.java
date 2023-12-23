package fr.uge.gitclout.git;

import fr.uge.gitclout.Contribution.Contribution;
import fr.uge.gitclout.Contribution.ContributionLoader;
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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

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
    Objects.requireNonNull(remoteURL);
    if (remoteURL.endsWith(".git")) {
      return remoteURL.substring(remoteURL.lastIndexOf("/") + 1, remoteURL.lastIndexOf("."));
    }
    return remoteURL.substring(remoteURL.lastIndexOf("/") + 1);
  }

  public static GitManager fromRepository(Repository repository) throws IOException {
    Objects.requireNonNull(repository);
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
    Objects.requireNonNull(directoryForRepoStorage);
    Path startDir = Paths.get(localPath.toString());
    List<Path> fileList = Files.walk(startDir).filter(Files::isRegularFile).toList();
    System.out.println("la liste des fichiers récupérés via walk : "+fileList);
    //var pattern = Pattern.compile(".*\\.com\\/(.*)");
    var files = fileList.stream().map(Path::toString).collect(Collectors.toList());
    //retrieve the part of the file name which is relevant

    //System.out.println("les fichiers après transformation "+files2);
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

  public ContributionLoader parseEveryFileFromCurrentRepoAndTransformItIntoContribution() throws GitAPIException, IOException {
    //the final lists with all hte contributions
    var contributionLoader = new ContributionLoader();
    int compteur = 0;
    var files = retrieveEveryFileFromRepo(localPath.toString());
    for (var file : files) {
      //System.out.println("le fichier suivant à traiter est : "+file);
      //need to be deleted (à supprimer).
      /*if (compteur == 30)   // stop after 30 files in order to not wait too much
          break;*/
        parseOneFileForEachTagWithContributors(contributionLoader, Path.of(file));
        //compteur++;
    }
    return contributionLoader;
  }


    /**
     *
     * @param contributionLoader
     * the list that will contain the contributions made by the users.
     * @param hashUserLine
     * the line written by each user.
     * @param hashUserCommentLine
     * the comment line written by each user.
     * @param documentation
     * it represents the kind of documentation used by the user.
     */
  public void fromMapToListContribution(ContributionLoader contributionLoader, HashMap<Contributor, Integer> hashUserLine, HashMap<Contributor, Integer> hashUserCommentLine, Documentation documentation, Ref tag) {
    Objects.requireNonNull(tag);
    Objects.requireNonNull(documentation);
    Objects.requireNonNull(hashUserLine);
    Objects.requireNonNull(contributionLoader);
    //feed code line
    fromMapToListLineContribution(contributionLoader, hashUserLine,  documentation, tag);
    //feed comments lines
    fromMapToListCommentContribution(contributionLoader, hashUserCommentLine,  documentation, tag);

  }

  /**
   *
   * this method feed the loader with the lines of actual contributions.
   * @param contributionLoader
   * the list that will contain the contributions made by the users.
   * @param hashUserLine
   * the comment lines written by each user.
   * @param documentation
   * it represents the kind of documentation used by the user.
   */
  public void fromMapToListLineContribution(ContributionLoader contributionLoader, HashMap<Contributor, Integer> hashUserLine, Documentation documentation, Ref tag) {
    Objects.requireNonNull(tag);Objects.requireNonNull(documentation); Objects.requireNonNull(hashUserLine); Objects.requireNonNull(contributionLoader);
    try {
      final var tag1 = new Tag(tag.getName().substring(tag.getName().lastIndexOf("/") + 1), tag.getName(), getTagDate(tag), repository);
      hashUserLine.forEach((user, lines) -> {
        var contribution = contributionLoader.getFromDescription(tag1,user,documentation);
        //check if the contribution already exists
        if(contribution.isPresent()) contribution.orElseThrow().increaseLine(lines,0);
          //case of new contribution
        else contributionLoader.add(new Contribution(user, tag1, documentation, 0, lines));
      });
    } catch (IOException e) { throw new RuntimeException(e); }

  }


  /**
   *
   * this method feed the loader with the lines of comment contributions.
   * @param contributionLoader
   * the list that will contain the contributions made by the users.
   * @param hashUserCommentLine
   * the comment lines written by each user.
   * @param hashUserCommentLine
   * the comment line written by each user.
   * @param documentation
   * it represents the kind of documentation used by the user.
   */
  public void fromMapToListCommentContribution(ContributionLoader contributionLoader, HashMap<Contributor, Integer> hashUserCommentLine, Documentation documentation, Ref tag) {
    Objects.requireNonNull(tag);Objects.requireNonNull(documentation); Objects.requireNonNull(hashUserCommentLine); Objects.requireNonNull(contributionLoader);
    try {
      final var tag1 = new Tag(tag.getName().substring(tag.getName().lastIndexOf("/") + 1), tag.getName(), getTagDate(tag), repository);
      hashUserCommentLine.forEach((user, lines) -> {
        var contribution = contributionLoader.getFromDescription(tag1,user,documentation);
        //check if the contribution already exists
        if(contribution.isPresent()) contribution.orElseThrow().increaseLine(0,lines);
        //case of new contribution
        else contributionLoader.add(new Contribution(user, tag1, documentation, lines, 0));
      });
    } catch (IOException e) { throw new RuntimeException(e); }

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
    Objects.requireNonNull(file);
    for(var document:documentations){
        var pattern = Pattern.compile(".*"+document.extension()+"$");
        var matcher = pattern.matcher(file.toString());
        if(matcher.find()){
          if(document.language().isPresent()) return document;
          else return new Documentation(null, document.fileType(),document.extension());
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
  public void parseOneFileForEachTagWithContributors(ContributionLoader contributionLoader,Path filePath) throws GitAPIException {
    Objects.requireNonNull(filePath, "the file you're parsing cannot be null"); Objects.requireNonNull(contributionLoader);
    //retrieve the document
    var document = fromFileToDocumentation(filePath);
    for (var tag : retrieveTags()) {
      //System.out.println(" on teste pour le tag :"+tag);
      //retrieve the actual lines of contribution and retrieve the comment line of contribution
      HashMap<Contributor, Integer> hashUserLine = new HashMap<Contributor, Integer>(); HashMap<Contributor, Integer> hashUserCommentLine = new HashMap<Contributor, Integer>();
      var blameResult = git.blame().setFilePath(filePath.toString()).setStartCommit(tag.getObjectId()).call();
      if (blameResult != null) {
        feedHashWithBlame(document, hashUserLine, hashUserCommentLine, blameResult);
        fromMapToListContribution(contributionLoader, hashUserLine,hashUserCommentLine, document, tag);
      }
//      else{
//        System.out.println("Le blame result est null pour le tag "+tag);
//      }
    }
  }

  /**
   * @param document
   * the document you use to know the kind of comment of your file.
   * Fill an hashMap "user, number of line"  with the number of line by user using the result of a blame.
   * And fill an hashMap "user, number of comment line" with the number of comment made by user using the result of a blame.
   *
   * This method assumes that a line that would contain some code and a comment would be considered as a code Line.
   * @param hashUserLines
   * the hashMap with user's non-comment line you want to fill
   * @param hashUserCommentLines
   * the hashMap with user's comment line you want to fill
   * @param blameResult
   * the result of the blame you made on a file.
   */
  public static void feedHashWithBlame(Documentation document, HashMap<Contributor, Integer> hashUserLines, HashMap<Contributor, Integer> hashUserCommentLines, BlameResult blameResult) {
    Objects.requireNonNull(document); Objects.requireNonNull(hashUserLines);Objects.requireNonNull(blameResult); Objects.requireNonNull(hashUserCommentLines);
    var containsComments = document.language().isPresent();
    //handle programming language
    if(containsComments) feedHashWithBlameForProgrammingLanguage(document, hashUserLines, hashUserCommentLines, blameResult);
    //handle non-programming language
      // this is not a programming language, then there's no comments
    else feedHashWithBlameForNonProgrammingLanguage(document, hashUserLines, blameResult);
  }

  /**
   * @param document
   * the document you use to know the kind of comment of your file.
   * Fill an hashMap "user, number of line"  with the number of line by user using the result of a blame.
   * And fill an hashMap "user, number of comment line" with the number of comment made by user using the result of a blame.
   *
   * This method assumes that a line that would contain some code and a comment would be considered as a code Line.
   * @param hashUserLines
   * the hashMap with user's comment line you want to fill
   * @param blameResult
   * the result of the blame you made on a file.
   */
  private static void feedHashWithBlameForNonProgrammingLanguage(Documentation document, HashMap<Contributor, Integer> hashUserLines, BlameResult blameResult){
    Objects.requireNonNull(document); Objects.requireNonNull(hashUserLines);Objects.requireNonNull(blameResult); Objects.requireNonNull(blameResult);
    for (int i = 0; i < blameResult.getResultContents().size(); i++) {
      hashUserLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
    }
  }

  /**
   * @param document
   * the document you use to know the kind of comment of your file.
   * Fill an hashMap "user, number of line"  with the number of line by user using the result of a blame.
   * And fill an hashMap "user, number of comment line" with the number of comment made by user using the result of a blame.
   *
   * This method assumes that a line that would contain some code and a comment would be considered as a code Line.
   * @param hashUserLines
   * the hashMap with user's non-comment line you want to fill
   * @param hashUserCommentLines
   * the hashMap with user's comment line you want to fill
   * @param blameResult
   * the result of the blame you made on a file.
   */
  public static void feedHashWithBlameForProgrammingLanguage(Documentation document, HashMap<Contributor, Integer> hashUserLines, HashMap<Contributor, Integer> hashUserCommentLines, BlameResult blameResult){
    Objects.requireNonNull(document); Objects.requireNonNull(hashUserLines);Objects.requireNonNull(hashUserCommentLines);
    Objects.requireNonNull(blameResult); Objects.requireNonNull(blameResult);
    //check if we are into a comment line
    var isInCommentMode = false;
    //this variable enable to know which comment we are using
    var currentCommentIndex = -1;
    for (int i = 0; i < blameResult.getResultContents().size(); i++) {
      //retrieve the current line content
      var lineToParse = blameResult.getResultContents().getString(i);
      //a multiline continue
      if(isInCommentMode && !document.language().orElseThrow().thisStringEndsComment(lineToParse,currentCommentIndex)){
        //the comments ends here
        isInCommentMode = false;
        currentCommentIndex =-1;
        //one last comment line
        hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);

      }
      //we start a single line comment
      else if(document.language().orElseThrow().thisStringStartsWithSingleLineComment(lineToParse)){
        isInCommentMode = false;
        //increase the number of comments
        hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
      }
      else{
        //check if a new multiline comment starts and doesn't finish on the same line
        currentCommentIndex = document.language().orElseThrow().thisStringEndsWithUnfinishedComment(lineToParse);
        if(currentCommentIndex > -1){
          //System.out.println(" un multi line comment commence "+lineToParse);
          //comment mode start
          isInCommentMode = true;
          hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
          //the line didn't start by a comment, then it start by code
          if(document.language().orElseThrow().thisStringStartsWithComment(lineToParse) != -1) hashUserLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);

        }

        //a multiline comment starts and ends on the same line at the start of the line
        else if(document.language().orElseThrow().thisStringStartsWithComment(lineToParse) != -1){
          hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
        }
        //code line (but not exhaustive should check if there's code between two comments
        else{
          hashUserLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()), 1, (oldValue, newValue) -> oldValue + 1);
          isInCommentMode = false;
        }

      }

    }

  }
  public static void main(String[] args) throws IOException, GitAPIException {

    try {

      /*projets tests :
      "https://github.com/vuejs/core"
      "https://github.com/facebookresearch/llama"*/
      //var repositoryPath = "https://github.com/vuejs/core";
      //"https://github.com/damo-vilab/AnyDoor";
      //https://github.com/LC044/WeChatMsg;
      //https://gitlab.ow2.org/asm/asm.git
      //var repositoryPath ="https://github.com/facebookresearch/llama";
      //"https://github.com/bruno00o/Patchwork"*/
      var repositoryPath = "https://github.com/vuejs/core";
      //var fileForAnalyze = "packages/global.d.ts";
      var fileForAnalyze = "scripts/aliases.js";
      //var fileForAnalyze = "Patchwork.iml";
      var handler2 = new GitManager(repositoryPath);
      var resContribution = handler2.parseEveryFileFromCurrentRepoAndTransformItIntoContribution();
      //var resContribution = new ContributionLoader();
      //handler2.parseOneFileForEachTagWithContributors(resContribution, Path.of(fileForAnalyze));
      //System.out.println("on teste seulement le fichier "+fileForAnalyze+"\n\n");
      for(var contribution:resContribution.contributions()){
        System.out.println(contribution);
      }


    } catch (GitAPIException | IOException e) {
      throw new RuntimeException(e);
    }

  }
}