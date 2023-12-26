package fr.uge.gitclout.repository.db;

import fr.uge.gitclout.repository.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@io.micronaut.data.annotation.Repository
public interface RepositoryRepository extends CrudRepository<Repository, String> {
  Optional<Repository> findByRepositoryURL(String repositoryURL);
}
