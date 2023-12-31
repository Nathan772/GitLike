package fr.uge.gitclout.tag;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

/**
 * The class managing the tags.
 */
public class TagManager {
  private final Repository repository;

  /**
   * The constructor of the class.
   *
   * @param repository The repository.
   */
  public TagManager(Repository repository) {
    this.repository = repository;
  }

  /**
   * Gets the tags of the repository.
   *
   * @return The list of tags.
   * @throws GitAPIException If an error occurred while getting the tags.
   */
  public List<Ref> getTags() throws GitAPIException {
    var git = new Git(repository);
    return git.tagList().call();
  }

}
