package fr.uge.gitclout.db;

import fr.uge.gitclout.model.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;

import java.util.Optional;

@io.micronaut.data.annotation.Repository
public interface RepositoryRepository extends ReactorCrudRepository<Repository, Long> {
  Optional<Repository> findByURL(String URL);
  Optional<Repository> findById(long id);
}
