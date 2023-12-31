package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the list of contributions.
 */
@Serdeable
public class Contributions {
  @JsonValue
  private final List<AuthorContributions> contributions = new ArrayList<>();

  /**
   * Adds a contribution for an author.
   *
   * @param author       the author
   * @param type         the type of contribution
   * @param language     the language
   * @param count        the number of lines
   * @param commentCount the number of lines of comments
   */
  public void addAuthorContribution(String author, ContributionType type, String language, int count, int commentCount) {
    AuthorContributions authorContributions = contributions.stream()
            .filter(c -> c.getAuthor().equals(author))
            .findFirst()
            .orElseGet(() -> {
              AuthorContributions newContributions = new AuthorContributions(author);
              contributions.add(newContributions);
              return newContributions;
            });
    authorContributions.addContribution(type, language, count, commentCount);
  }

  /**
   * Get the list of contributions.
   *
   * @return the list of contributions
   */
  public List<AuthorContributions> getContributions() {
    return contributions;
  }
}