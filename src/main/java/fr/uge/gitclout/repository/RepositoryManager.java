package fr.uge.gitclout.repository;

import fr.uge.gitclout.repository.db.RepositoryRepository;
import jakarta.inject.Singleton;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * The class managing the repositories.
 */
@Singleton
public class RepositoryManager {
  private final RepositoryRepository repositoryRepository;
  private final Path repositoryPath = Path.of("repositories");

  public RepositoryManager(RepositoryRepository repositoryRepository) {
    Objects.requireNonNull(repositoryRepository);
    this.repositoryRepository = repositoryRepository;
  }

  /**
   * Clones a repository in Bare mode.
   *
   * @param repositoryURL The URL of the repository.
   * @param path          The path where the repository will be cloned.
   * @throws GitAPIException If an error occurred while cloning the repository.
   */
  private void cloneRepository(String repositoryURL, Path path) throws GitAPIException {
    Git.cloneRepository()
            .setURI(repositoryURL)
            .setDirectory(path.toFile())
            .setBare(true)
            .call();
  }

  /**
   * Gets the repository name from its URL.
   *
   * @param repositoryURL The URL of the repository.
   * @return The repository name.
   * @throws URISyntaxException If the repository URL is not valid.
   */
  private String getRepositoryName(String repositoryURL) throws URISyntaxException {
    Objects.requireNonNull(repositoryURL);
    URIish uri = new URIish(repositoryURL);
    return uri.getHumanishName();
  }

  /**
   * Opens a repository.
   *
   * @param path The path of the repository.
   * @throws IOException If an error occurred while opening the repository.
   */
  private void openRepository(Path path) throws IOException {
    Git.open(path.toFile());
  }

  /**
   * Generates a local path for a repository.
   *
   * @param repositoryURL The URL of the repository.
   * @return The local path of the repository.
   * @throws URISyntaxException If the repository URL is not valid.
   */
  private Path generateRepositoryLocalPath(String repositoryURL) throws URISyntaxException {
    var timestamp = System.currentTimeMillis();
    return repositoryPath.resolve(getRepositoryName(repositoryURL) + "-" + timestamp);
  }

  /**
   * Opens or creates a repository.
   *
   * @param repositoryURL The URL of the repository.
   * @param path          The path of the repository.
   * @throws GitAPIException If an error occurred while opening or creating the repository.
   */
  private void openOrCreateRepository(String repositoryURL, Path path) throws GitAPIException {
    try {
      openRepository(path);
    } catch (IOException e) {
      cloneRepository(repositoryURL, path);
    }
  }

  /**
   * Resolves a repository.
   *
   * @param repositoryURL The URL of the repository.
   * @return The repository.
   * @throws GitAPIException    If an error occurred while resolving the repository.
   * @throws URISyntaxException If the repository URL is not valid.
   * @throws IOException        If an error occurred while resolving the repository.
   */
  public Repository resolveRepository(String repositoryURL) throws GitAPIException, URISyntaxException, IOException {
    Objects.requireNonNull(repositoryURL);
    var repository = repositoryRepository.findByRepositoryURL(repositoryURL);
    if (repository.isPresent()) {
      openOrCreateRepository(repositoryURL, Path.of(repository.get().getRepositoryLocalPath()));
      return new FileRepository(Path.of(repository.get().getRepositoryLocalPath()).toFile());
    } else {
      var repositoryLocalPath = generateRepositoryLocalPath(repositoryURL);
      cloneRepository(repositoryURL, repositoryLocalPath);
      return new FileRepository(repositoryLocalPath.toFile());
    }
  }
}
