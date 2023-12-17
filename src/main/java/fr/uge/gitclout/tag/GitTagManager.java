package fr.uge.gitclout.tag;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.List;

public class GitTagManager {
  private final Repository repository;

  public GitTagManager(Repository repository) {
    this.repository = repository;
  }

  public List<Ref> getTags() throws GitAPIException, IOException {
    var git = new Git(repository);
    return git.tagList().call();
  }

}
