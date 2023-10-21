package fr.uge.gitclout.jgithandler;

import fr.uge.gitclout.repositories.Contributor;
import fr.uge.gitclout.repositories.Tag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.List;
import java.util.Objects;

public class Handler {
  // private final Git git;

  public Handler(String url) throws GitAPIException {
    Objects.requireNonNull(url);
    /* git = Git.cloneRepository()
            .setURI(url)
            .setDirectory(new File("/tmp/test"))
            .call();
     */
  }

  /**
   * Close the git repository.
   */
  public void close() {
    //git.close();
  }

  public List<Contributor> getContributors(Tag tag) {
    return null;
  }
}
