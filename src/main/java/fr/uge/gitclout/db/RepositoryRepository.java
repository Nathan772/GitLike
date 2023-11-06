package fr.uge.gitclout.db;

import fr.uge.gitclout.model.Repository;
import io.micronaut.data.repository.CrudRepository;

@io.micronaut.data.annotation.Repository
public interface RepositoryRepository extends CrudRepository<Repository, Long> {
  Repository find(String name);
}
