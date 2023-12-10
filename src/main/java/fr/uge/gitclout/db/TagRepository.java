package fr.uge.gitclout.db;

import fr.uge.gitclout.model.Tag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Flux;

@Repository
public interface TagRepository extends ReactorCrudRepository<Tag, Long> {
//  public List<Tag> findAllByRepositoryId(Long id);

  Flux<Tag> findAllByRepositoryId(Long id);
}
