package fr.uge.gitclout.repository.db;

import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

/**
 * The interface for database management of repositories.
 */
@io.micronaut.data.annotation.Repository
public interface RepositoryRepository extends CrudRepository<Repository, String> {

  /**
   * Finds a repository by its URL.
   *
   * @param repositoryURL The URL of the repository.
   * @return The repository.
   */
  Optional<Repository> findByRepositoryURL(String repositoryURL);
}
