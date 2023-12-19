package fr.uge.gitclout.db;

import fr.uge.gitclout.model.Repository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.Objects;

@Singleton
public class DatabaseManager {
  @Inject
  private RepositoryRepository repositoryRepository;

  @Transactional
  public void saveRepository(Repository repository) {
    Objects.requireNonNull(repository);
    repositoryRepository.save(repository);
  }


}
