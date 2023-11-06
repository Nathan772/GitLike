package fr.uge.gitclout.db;

import fr.uge.gitclout.model.Repository;
import jakarta.inject.Inject;

public class DatabaseManager {
  @Inject
  private RepositoryRepository repositoryRepository;

  public void saveRepository(Repository repository) {
    repositoryRepository.save(repository);
  }
}
