package fr.uge.gitclout.jgithandler;

import fr.uge.gitclout.repositories.Contributor;
import fr.uge.gitclout.repositories.Tag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawTextComparator;

import java.io.File;
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

  }

  /**
   *
   * This method perfrom a blame on the file given as an argument.
   *
   * @param file
   * file you want to parse
   *
   */
  public BlameResult retrieveBlameFileResult(String file) throws GitAPIException {
    Objects.requireNonNull(file, "the file analyzed by blame cannot be null");
   //File f = new File(file);
    /*if(f.exists() && !f.isDirectory()){
      System.out.println("le fichier "+file+" a bien été trouvé");
    }*/
    return git.blame().setFilePath(file).setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
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
    //on fait des tests
    try {
      var handler = new Handler("https://github.com/NathanBil/Splendor_for_visitors.git");
      var blameResult = handler.retrieveBlameFileResult("tmp/test/Affichage.java");
      if (blameResult != null) {
        int rows = blameResult.getResultContents().size();
        //List<BlamedLine> result = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
          System.out.println(blameResult.getResultContents().getString(i));
          System.out.println(blameResult.getSourceAuthor(i).getName());
          System.out.println(blameResult.getSourceCommitter(i).getName());
          System.out.println(blameResult.getSourceCommit(i).getId().getName());
        }
      } else {
        throw new RuntimeException("BlameResult not found.");
      }

      handler.close();
    } catch (GitAPIException e) {
      throw new RuntimeException(e);
    }
  }


}
