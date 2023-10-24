package fr.uge.gitclout.jgithandler;

import fr.uge.gitclout.repositories.Contributor;
import fr.uge.gitclout.repositories.Tag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Handler {
  // private final Git git;
  private final Git git;


  public Handler(String url) throws GitAPIException {
    Objects.requireNonNull(url);
    git = Git.cloneRepository()
            .setURI(url)
            .setDirectory(new File("tmp/test")) // gives the path for the location of the directory
            .call();
    //Git git2 = Git.cloneRepository().setURI(url);

  }

  /**
   * Constructor that enables to open an already existing project
   * @param projectPath
   * The path for existing project
   * @param exists
   * precise if the project already exists or not
   * @throws GitAPIException
   * @throws IOException
   */
  public Handler(String projectPath, boolean exists) throws GitAPIException, IOException {
    Objects.requireNonNull(projectPath);
    //open an already existing project
    git = Git.open(new File(projectPath));

  }

  /**
   *
   * This method performs a blame on the file given as an argument.
   *
   * @param filePath
   * The file you want to parse.
   * You have to give the path from the project repository until the file itself.
   *
   */
  public BlameResult retrieveBlameFileResult(String filePath) throws GitAPIException {
    Objects.requireNonNull(filePath, "the file analyzed by blame cannot be null");
    return git.blame().setFilePath(filePath).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
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

  public static String parseTag(Ref tag){
     return tag.getName();
  }

  /**
   *
   * This method performs a blame on the file given as an argument and
   * create a tuple with the number of lines for each user associated to the file.
   *
   * @param filePath
   * The file you want to parse.
   * You have to give the path from the project repository until the file itself.
   *
   */
  public HashMap<String, Integer> givesUsersLines(String filePath) throws GitAPIException {
    var blameResult = retrieveBlameFileResult(filePath);
    HashMap<String, Integer> hashUserLine = new HashMap<String, Integer>();
    //retrieve the total of rows of the blame
    for (int i = 0; i < blameResult.getResultContents().size();i++) {
        //create a tuple for this user
        hashUserLine.computeIfAbsent(blameResult.getSourceAuthor(i).getName(), (k)-> 1);
        //increase by one the number of line for this user
        hashUserLine.computeIfPresent(blameResult.getSourceAuthor(i).getName(), (k,v)->v+1);
    }

    return hashUserLine;


  }

  /**
   * Close the git repository.
   */
  public void close() {
    git.close();
  }

  public List<Contributor> getContributors(Tag tag) {
    return null;
  }

  public static void main(String args[]) {

    HashMap<String,Integer> hmUsers;
    var res2 = new StringBuilder();
    try {
      //var handler = new Handler("https://github.com/vuejs/core.git");
      //https://gitlab.com/seilliebert-bilingi/SEILLIEBERT-BILINGI.git
      var handler2 = new Handler("tmp/test/", true);
      //var blameResult = handler.retrieveBlameFileResult("Affichage.java");
      //hmUsers = handler.givesUsersLines("Affichage.java");
      var listTags = handler2.retrieveTags();

      for(var tmp:listTags){
        res2.append(parseTag(tmp)).append(";");
      }
    } catch (GitAPIException | IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("On affiche les tags "+res2);
    //System.out.println("On affiche la hashMap "+hmUsers);
  }




}
