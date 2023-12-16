package fr.uge.gitclout.tag;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class GitTagManager {
  private final Path repositoryPath;

  public GitTagManager(Path repositoryPath) {
    this.repositoryPath = repositoryPath;
  }

  public List<String> getTags() throws GitAPIException, IOException {
    var git = Git.open(repositoryPath.toFile());
    var tags = git.tagList().call();
    return tags.stream().map(Ref::getName).collect(Collectors.toList());
  }

}
