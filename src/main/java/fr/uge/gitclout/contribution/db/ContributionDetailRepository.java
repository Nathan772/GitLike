package fr.uge.gitclout.contribution.db;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

/**
 * The class representing a contribution detail in the database.
 */
@Repository
public interface ContributionDetailRepository extends CrudRepository<ContributionDetail, Long> {

  /**
   * Returns the contribution details of a contribution.
   *
   * @param contributionId The id of the contribution.
   * @return The contribution details of a contribution.
   */
  List<ContributionDetail> findByContributionId(Long contributionId);
}
