package fr.uge.gitclout.db;

import fr.uge.gitclout.model.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@io.micronaut.data.annotation.Repository
public interface RepositoryRepository extends CrudRepository<Repository, Long> {
  Optional<Repository> findByURL(String URL);
}
