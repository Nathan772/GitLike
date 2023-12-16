package fr.uge.gitclout.repository.infos;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.URIish;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

public class GitRepositoryInfosManager {
  private final String repositoryURL;

  public GitRepositoryInfosManager(String repositoryURL) {
    this.repositoryURL = repositoryURL;
  }

  private Collection<Ref> getRemoteRepositoryRefs() throws GitAPIException {
    try {
      return Git.lsRemoteRepository().setRemote(repositoryURL).call();
    } catch (JGitInternalException e) {
      return List.of();
    }
  }

  public boolean doesRemoteRepositoryExists() {
    try {
      Collection<Ref> lsRemote = getRemoteRepositoryRefs();
      return !lsRemote.isEmpty();
    } catch (GitAPIException e) {
      return false;
    }
  }

  private String getRepositoryName() throws URISyntaxException {
    URIish uri = new URIish(repositoryURL);
    return uri.getHumanishName();
  }

  private List<String> getRemoteRepositoryTags() throws GitAPIException {
    return getRemoteRepositoryRefs().stream()
            .filter(ref -> ref.getName().startsWith("refs/tags/"))
            .map(ref -> ref.getName().replace("refs/tags/", ""))
            .sorted()
            .toList().reversed();
  }

  public RepositoryInfos getRepositoryInfos() throws URISyntaxException, GitAPIException {
    return new RepositoryInfos(getRepositoryName(), repositoryURL, getRemoteRepositoryTags());
  }
}

