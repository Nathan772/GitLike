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
import java.util.Objects;

/**
 * The class managing a remote repository.
 */
public class GitRemoteRepositoryManager {
  private final String repositoryURL;

  /**
   * Creates a new remote repository manager.
   *
   * @param repositoryURL The URL of the remote repository.
   */
  public GitRemoteRepositoryManager(String repositoryURL) {
    Objects.requireNonNull(repositoryURL);
    this.repositoryURL = repositoryURL;
  }

  /**
   * Gets the remote repository refs.
   *
   * @return The remote repository refs.
   * @throws GitAPIException If an error occurred while getting the remote repository refs.
   */
  private Collection<Ref> getRemoteRepositoryRefs() throws GitAPIException {
    try {
      return Git.lsRemoteRepository().setRemote(repositoryURL).call();
    } catch (JGitInternalException e) {
      return List.of();
    }
  }

  /**
   * Checks if a remote repository exists and is accessible.
   *
   * @return true if the remote repository exists and is accessible, false otherwise.
   */
  public boolean doesRemoteRepositoryExists() {
    try {
      Collection<Ref> lsRemote = getRemoteRepositoryRefs();
      return !lsRemote.isEmpty();
    } catch (GitAPIException e) {
      return false;
    }
  }

  /**
   * Gets the remote repository name.
   *
   * @return The remote repository name.
   * @throws URISyntaxException If an error occurred while getting the remote repository name.
   */
  private String getRepositoryName() throws URISyntaxException {
    URIish uri = new URIish(repositoryURL);
    return uri.getHumanishName();
  }

  /**
   * Gets the remote repository tags.
   *
   * @return The remote repository tags.
   * @throws GitAPIException If an error occurred while getting the remote repository tags.
   */
  private List<String> getRemoteRepositoryTags() throws GitAPIException {
    return getRemoteRepositoryRefs().stream()
            .filter(ref -> ref.getName().startsWith("refs/tags/"))
            .map(ref -> ref.getName().replace("refs/tags/", ""))
            .sorted()
            .toList();
  }

  /**
   * Gets the remote repository information.
   *
   * @return The remote repository information.
   * @throws URISyntaxException If an error occurred while getting the remote repository information.
   * @throws GitAPIException    If an error occurred while getting the remote repository information.
   */
  public JSONRepository getRepositoryInfos() throws URISyntaxException, GitAPIException {
    return new JSONRepository(getRepositoryName(), repositoryURL, getRemoteRepositoryTags());
  }
}

