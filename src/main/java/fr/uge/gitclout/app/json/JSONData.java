package fr.uge.gitclout.app.json;

import fr.uge.gitclout.contribution.AuthorContributions;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

/**
 * The interface representing a JSON data.
 */
@Serdeable
public interface JSONData {

  /**
   * The record representing a JSON repository.
   *
   * @param name name of the repository
   * @param URL  URL of the repository
   * @param tags list of tags
   */
  @Serdeable
  record JSONRepository(
          String name,
          String URL,
          List<String> tags
  ) implements JSONData {
  }

  /**
   * The record representing a JSON URL.
   *
   * @param URL URL of the repository
   */
  @Serdeable
  record JSONUrl(
          String URL
  ) implements JSONData {
  }

  /**
   * The record representing a JSON contributions.
   *
   * @param tagName       name of the tag
   * @param contributions list of author contributions
   */
  @Serdeable
  record JSONContributions(
          String tagName,
          List<AuthorContributions> contributions
  ) implements JSONData {
  }

  /**
   * The record representing a JSON history.
   *
   * @param repositories list of repositories
   */
  @Serdeable
  record JSONHistory(
          List<JSONRepository> repositories
  ) implements JSONData {
  }
}
