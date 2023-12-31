package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the number of lines and comments for a language.
 */
@Serdeable
public class ContributionTypeDetails {
  @JsonProperty("total")
  private int total;
  @JsonProperty("totalComment")
  private int totalComment;
  @JsonProperty("details")
  private final Map<String, LanguageCount> details = new HashMap<>();

  /**
   * Creates a new LanguageCount.
   */
  public ContributionTypeDetails() {
  }

  /**
   * Creates a new LanguageCount.
   *
   * @param total        the number of lines
   * @param totalComment the number of lines of comments
   * @param details      the details
   */
  @JsonCreator
  public ContributionTypeDetails(int total, int totalComment, Map<String, LanguageCount> details) {
    Objects.requireNonNull(details);
    if (total < 0) {
      throw new IllegalArgumentException("total contribution cannot be lesser than 0");
    }
    this.total = total;
    this.totalComment = totalComment;
    this.details.putAll(details);
  }

  /**
   * Adds a contribution for a language.
   *
   * @param language     the language
   * @param count        the number of lines
   * @param commentCount the number of lines of comments
   */
  public void addContribution(String language, int count, int commentCount) {
    LanguageCount languageCount = details.computeIfAbsent(language, LanguageCount::new);
    languageCount.incrementCount(count, commentCount);
    total += count;
    totalComment += commentCount;
  }

  /**
   * Returns the total number of lines.
   *
   * @return the total number of lines
   */
  public int getTotal() {
    return total;
  }

  /**
   * Returns the total number of lines of comments.
   *
   * @return the total number of lines of comments
   */
  public int getTotalComment() {
    return totalComment;
  }

  /**
   * Returns the details.
   *
   * @return the details
   */
  public Map<String, LanguageCount> getDetails() {
    return details;
  }
}
