package fr.uge.gitclout.tag;

import fr.uge.gitclout.tag.Tag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
//  public List<Tag> findAllByRepositoryId(Long id);

  //List<Tag> findAllByRepositoryId(Long id);
}
