package fr.uge.gitclout.tag.db;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface for database management of tags.
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

  /**
   * Finds a tag by its name and the URL of its repository.
   *
   * @param tagName       The name of the tag.
   * @param repositoryURL The URL of the repository.
   * @return Optional of the tag.
   */
  Optional<Tag> findByTagNameAndRepositoryRepositoryURL(String tagName, String repositoryURL);

  /**
   * Finds all the tags of a repository.
   *
   * @param repositoryURL The URL of the repository.
   * @return The list of tags.
   */
  List<Tag> findByRepositoryRepositoryURL(String repositoryURL);
}
