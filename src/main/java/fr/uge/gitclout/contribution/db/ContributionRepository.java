package fr.uge.gitclout.contribution.db;

import fr.uge.gitclout.tag.db.Tag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

/**
 * The class representing a contribution in the database.
 */
@Repository
public interface ContributionRepository extends CrudRepository<Contribution, Long> {

  /**
   * Returns the contributions of a tag and a repository.
   *
   * @param tag        The tag of the contributions.
   * @param repository The repository of the contributions.
   * @return The contributions of a tag and a repository.
   */
  List<Contribution> findByTagAndRepository(Tag tag, fr.uge.gitclout.repository.db.Repository repository);
}
