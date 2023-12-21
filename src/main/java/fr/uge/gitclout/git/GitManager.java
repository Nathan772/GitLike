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
    //int compteurFile = 10;
    var files = retrieveEveryFileFromRepo(localPath.toString());
    for (var file : files) {
      //System.out.println("le fichier analysé : "+file);
      //need to be deleted (à supprimer).
      if (compteur == 30) // stop after 30 files in order to not wait too much
        break;
      var listContributes = parseOneFileForEachTagWithContributors(Path.of(file));

      contributesList.addAll(listContributes.contributions());
      if (!listContributes.contributions().isEmpty())
        compteur++;
    }
    return contributesList;
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
    var containsComments = document.language().isPresent();
    //this variable enable to know if the program is parsing commentLine
    var isInCommentMode = false;
    //this variable enable to know which comment we are using
    var currentCommentIndex = -1;
      for (int i = 0; i < blameResult.getResultContents().size(); i++) {
        //specific case of languages possessing comments
        if(containsComments) {
          //the line you want to parse
          var lineToParse = blameResult.getResultContents().getString(i);
          //case with end of a multiline comment
          if(isInCommentMode && !document.language().orElseThrow().thisStringEndsComment(lineToParse, currentCommentIndex)){
            //add a new line for comments
            hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(),
                    blameResult.getSourceAuthor(i).getEmailAddress()),0,(oldValue, newValue) -> oldValue+1);
            //System.out.println("increase the number of comment line for "+blameResult.getSourceAuthor(i));
            //System.out.println(hashUserCommentLines);
            isInCommentMode = false;
          }
          //check if a new comment starts
          else if(!isInCommentMode){
            currentCommentIndex = document.language().orElseThrow().thisStringStartsWithComment(lineToParse);
            // a new comment starts here at the beginning of the line
            if(currentCommentIndex != -1){
                // an ew comment has started
                isInCommentMode = true;
                //we remove the first part of the comment in order to not suppose a comment ends sooner than it actually does
                /* this apply for very specific cases just like """ """ in python because the beginning and the end of the comment
                are represented by the same string */
                lineToParse = lineToParse.replaceFirst(document.language().orElseThrow().endCommentRegex().get(currentCommentIndex), "");
                hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(),
                      blameResult.getSourceAuthor(i).getEmailAddress()),0,(oldValue, newValue) -> oldValue+1);
                //System.out.println("increase the number of comment line for "+blameResult.getSourceAuthor(i));
                //System.out.println(hashUserCommentLines);
              //the comment finish on the same line here
              if(!document.language().orElseThrow().thisStringEndsComment(lineToParse, currentCommentIndex)){
                isInCommentMode = false;
              }
            }
            //case where the line doesn't start with a comment but with some code
            else{
              //increase the line of code
              hashUserLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()),0,(oldValue, newValue) -> oldValue+1);
            }
            //check if there's a comment at the end of the line.
            if(!isInCommentMode){
              var lineAlreadyAdded = currentCommentIndex >= 0;
              // a comment on the same line starts but doesn't end.
              currentCommentIndex = document.language().orElseThrow().thisStringEndsWithUnfinishedComment(lineToParse);
              if(currentCommentIndex != -1){
                isInCommentMode = true;
                // a new comment is added.
                if(!lineAlreadyAdded) {
                  hashUserCommentLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(),
                          blameResult.getSourceAuthor(i).getEmailAddress()), 0, (oldValue, newValue) -> oldValue + 1);
                  //System.out.println("increase the number of comment line for "+blameResult.getSourceAuthor(i));
                }
              }

            }


          }
        }
        //this is not a programming language, then there's no comments
        else
          hashUserLines.merge(new Contributor(blameResult.getSourceAuthor(i).getName(), blameResult.getSourceAuthor(i).getEmailAddress()),0,(oldValue, newValue) -> oldValue+1);

    }
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
    var tag1 = new Tag(tag.getName(), tag.getName(), new Date(), repository);

    hashUserLine.forEach((user, lines) -> {
            var contribution = contributionLoader.getFromDescription(tag1,user,documentation);
            //check if the contribution already exists
            if(contribution.isPresent())  contribution.orElseThrow().increaseLine(lines, hashUserCommentLine.getOrDefault(user, 0));
            //case of new contribution
            else contributionLoader.add(new Contribution(user, tag1, documentation , hashUserCommentLine.getOrDefault(user, 0), lines));
    });

    //contributions.add(new Contribution(user, tag1, documentation , hashUserCommentLine.getOrDefault(user, 0), lines))

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
    //System.out.println("le fichier analysé est : "+file.toString()+"\n\n");
    for(var document:documentations){
        var pattern = Pattern.compile(".*"+document.extension());
        //System.out.println("l'extension testée est : "+document.extension());
        var matcher = pattern.matcher(file.toString());
        if(matcher.find()){
          if(document.language().isPresent()) {
            //System.out.println("on a reconnu un langage qui est : "+document.language());
            //return new Documentation(document.language().orElseThrow(), document.fileType(),document.extension());
            return document;
          }
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
  public ContributionLoader parseOneFileForEachTagWithContributors(Path filePath) throws GitAPIException {
    Objects.requireNonNull(filePath, "the file you're parsing cannot be null");
    //retrieve the document
    var document = fromFileToDocumentation(filePath);
    //retrieve the actual lines of contribution
    HashMap<Contributor, Integer> hashUserLine = new HashMap<Contributor, Integer>();
    //retrieve the comment line of contribution
    HashMap<Contributor, Integer> hashUserCommentLine = new HashMap<Contributor, Integer>();
    var listContributes = new ContributionLoader();
    for (var tag : retrieveTags()) {

      var blameResult = git.blame().setFilePath(filePath.toString()).setStartCommit(tag.getObjectId()).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
      if (blameResult != null) {
        feedHashWithBlame(document, hashUserLine, hashUserCommentLine ,blameResult);
        fromMapToListContribution(listContributes, hashUserLine,hashUserCommentLine, document, tag);
      }
    }
    return listContributes;
  }


  public static void main(String[] args) throws IOException, GitAPIException {

    try {
      /*"https://github.com/vuejs/core"
      "https://github.com/facebookresearch/llama"*/
      //var repositoryPath = "https://github.com/vuejs/core";
      //"https://github.com/damo-vilab/AnyDoor";
      //https://github.com/LC044/WeChatMsg;
      //https://gitlab.ow2.org/asm/asm.git
      var repositoryPath ="https://github.com/facebookresearch/llama";
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