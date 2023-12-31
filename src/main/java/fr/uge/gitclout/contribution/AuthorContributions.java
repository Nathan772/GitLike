package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing the contributions of an author.
 */
@Serdeable
public class AuthorContributions {
  @JsonProperty("author")
  private final String author;
  @JsonProperty("contributionsByType")
  private final Map<ContributionType, ContributionTypeDetails> contributionsByType = new HashMap<>();

  /**
   * Creates a new AuthorContributions.
   *
   * @param author              the author
   * @param contributionsByType the contributions by type
   */
  @JsonCreator
  public AuthorContributions(String author, Map<ContributionType, ContributionTypeDetails> contributionsByType) {
    Objects.requireNonNull(author);
    Objects.requireNonNull(contributionsByType);
    this.author = author;
    this.contributionsByType.putAll(contributionsByType);
  }

  /**
   * Creates a new AuthorContributions.
   *
   * @param author the author
   */
  public AuthorContributions(String author) {
    Objects.requireNonNull(author);
    this.author = author;
  }

  /**
   * Adds a contribution for a language.
   *
   * @param type         the type of contribution
   * @param language     the language
   * @param count        the number of lines
   * @param commentCount the number of lines of comments
   */
  public void addContribution(ContributionType type, String language, int count, int commentCount) {
    Objects.requireNonNull(type);
    if (count < 0) throw new IllegalArgumentException("count cannot be lesser than 0");
    if (commentCount < 0) throw new IllegalArgumentException("comment count cannot be lesser than 0");
    Objects.requireNonNull(language);
    ContributionTypeDetails typeDetails = contributionsByType.computeIfAbsent(type, k -> new ContributionTypeDetails());
    typeDetails.addContribution(language, count, commentCount);
  }

  /**
   * Adds a contribution for a language.
   *
   * @param type        the type of contribution
   * @param typeDetails the type details
   */
  public void addContribution(ContributionType type, ContributionTypeDetails typeDetails) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(typeDetails);
    contributionsByType.put(type, typeDetails);
  }

  /**
   * Returns the author.
   *
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Returns the contributions by type.
   *
   * @return the contributions by type
   */
  public Map<ContributionType, ContributionTypeDetails> getContributionsByType() {
    return contributionsByType;
  }
}
