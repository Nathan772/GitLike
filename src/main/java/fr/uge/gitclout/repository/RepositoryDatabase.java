package fr.uge.gitclout.repository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class RepositoryDatabase {
  private final RepositoryRepository repositoryRepository;

  @Inject
  public RepositoryDatabase(RepositoryRepository repositoryRepository) {
    this.repositoryRepository = repositoryRepository;
  }

  public Repository save(Repository repository) {
    return repositoryRepository.save(repository);
  }

  public Optional<Repository> findByRepositoryURL(String repositoryURL) {
    return repositoryRepository.findByRepositoryURL(repositoryURL);
  }
}
