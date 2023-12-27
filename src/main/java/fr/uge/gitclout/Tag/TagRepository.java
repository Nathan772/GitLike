package fr.uge.gitclout.Tag;

import fr.uge.gitclout.repository.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, String> {
    Optional<Tag> findTagByURL(String URL);
}
