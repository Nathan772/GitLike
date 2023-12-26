package fr.uge.gitclout.repository.remote;

import fr.uge.gitclout.app.json.JSONData.JSONRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.URIish;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

public class GitRemoteRepositoryManager {
  private final String repositoryURL;

  public GitRemoteRepositoryManager(String repositoryURL) {
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
            .toList();
  }

  public JSONRepository getRepositoryInfos() throws URISyntaxException, GitAPIException {
    return new JSONRepository(getRepositoryName(), repositoryURL, getRemoteRepositoryTags());
  }
}

