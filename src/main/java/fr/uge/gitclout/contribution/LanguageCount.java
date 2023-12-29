package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public class LanguageCount {
  @JsonProperty("language")
  private final String language;
  @JsonProperty("count")
  private int count;
  @JsonProperty("commentCount")
  private int commentCount;

  public LanguageCount() {
    this.language = "";
  }

  @JsonCreator
  public LanguageCount(@JsonProperty("language") String language,
                       @JsonProperty("count") int count,
                       @JsonProperty("commentCount") int commentCount) {
    Objects.requireNonNull(language);
    if(count < 0) throw new IllegalArgumentException("count cannot be lesser than 0");
    if(commentCount < 0) throw new IllegalArgumentException("commentCount cannot be lesser than 0");
    this.language = language;
    this.count = count;
    this.commentCount = commentCount;
  }

  public LanguageCount(String language) {
    this.language = language;
    this.count = 0;
    this.commentCount = 0;
  }

  public void incrementCount(int lineIncrement, int commentIncrement) {
    this.count += lineIncrement;
    this.commentCount += commentIncrement;
  }

  public String getLanguage() {
    return language;
  }

  public int getCount() {
    return count;
  }

  public int getCommentCount() {
    return commentCount;
  }

  @Override
  public String toString() {
    return "LanguageCount{" +
            "language='" + language + '\'' +
            ", count=" + count +
            ", commentCount=" + commentCount +
            '}';
  }
}
