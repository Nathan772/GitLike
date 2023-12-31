package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

/**
 * Represents the number of lines and comments for a language.
 */
@Serdeable
public class LanguageCount {
  @JsonProperty("language")
  private final String language;
  @JsonProperty("count")
  private int count;
  @JsonProperty("commentCount")
  private int commentCount;

  /**
   * Creates a new LanguageCount.
   *
   * @param language     the language
   * @param count        the number of lines
   * @param commentCount the number of lines of comments
   */
  @JsonCreator
  public LanguageCount(String language, int count, int commentCount) {
    Objects.requireNonNull(language);
    if (count < 0) throw new IllegalArgumentException("count cannot be lesser than 0");
    if (commentCount < 0) throw new IllegalArgumentException("commentCount cannot be lesser than 0");
    this.language = language;
    this.count = count;
    this.commentCount = commentCount;
  }

  /**
   * Creates a new LanguageCount.
   *
   * @param language the language
   */
  public LanguageCount(String language) {
    this.language = language;
    this.count = 0;
    this.commentCount = 0;
  }

  /**
   * Increments the number of lines and comments.
   *
   * @param lineIncrement    the number of lines to add
   * @param commentIncrement the number of lines of comments to add
   */
  public void incrementCount(int lineIncrement, int commentIncrement) {
    this.count += lineIncrement;
    this.commentCount += commentIncrement;
  }

  /**
   * Returns the language.
   *
   * @return the language
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Returns the number of lines.
   *
   * @return the number of lines
   */
  public int getCount() {
    return count;
  }

  /**
   * Returns the number of lines of comments.
   *
   * @return the number of lines of comments
   */
  public int getCommentCount() {
    return commentCount;
  }
}
