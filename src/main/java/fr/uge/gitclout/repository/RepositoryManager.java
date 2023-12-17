package fr.uge.gitclout.repository;

import jakarta.inject.Singleton;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

@Singleton
public class RepositoryManager {
  private Git git;

  private final RepositoryRepository repositoryRepository;

  private final Path repositoryPath = Path.of("repositories");

  public RepositoryManager(RepositoryRepository repositoryRepository) {
    this.repositoryRepository = repositoryRepository;
  }

  private void cloneRepository(String repositoryURL, Path path) throws GitAPIException {
    git = Git.cloneRepository()
            .setURI(repositoryURL)
            .setDirectory(path.toFile())
            .setBare(true)
            .call();
  }

  private String getRepositoryName(String repositoryURL) throws URISyntaxException {
    URIish uri = new URIish(repositoryURL);
    return uri.getHumanishName();
  }

  private void openRepository(Path path) throws GitAPIException, IOException {
    git = Git.open(path.toFile());
  }

  private Path generateRepositoryLocalPath(String repositoryURL) throws URISyntaxException {
    var timestamp = System.currentTimeMillis();
    return repositoryPath.resolve(getRepositoryName(repositoryURL) + "-" + timestamp);
  }

  public Repository resolveRepository(String repositoryURL) throws GitAPIException, URISyntaxException, IOException {
    var repository = repositoryRepository.findByRepositoryURL(repositoryURL);
    if (repository.isPresent()) {
      try {
        openRepository(Path.of(repository.get().getRepositoryLocalPath()));
      } catch (IOException e) {
        cloneRepository(repositoryURL, Path.of(repository.get().getRepositoryLocalPath()));
      }
      return new FileRepository(Path.of(repository.get().getRepositoryLocalPath()).toFile());
    } else {
      var repositoryLocalPath = generateRepositoryLocalPath(repositoryURL);
      cloneRepository(repositoryURL, repositoryLocalPath);
      return new FileRepository(repositoryLocalPath.toFile());
    }
  }
}
