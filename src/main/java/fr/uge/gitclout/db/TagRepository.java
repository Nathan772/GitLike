package fr.uge.gitclout.db;

import fr.uge.gitclout.model.Tag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
  public List<Tag> findAllByRepositoryId(Long id);
}
