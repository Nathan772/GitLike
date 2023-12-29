package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Serdeable
public class ContributionTypeDetails {
  @JsonProperty("total")
  private int total;
  @JsonProperty("totalComment")
  private int totalComment;
  @JsonProperty("details")
  private final Map<String, LanguageCount> details = new HashMap<>();

  public ContributionTypeDetails() {
  }

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

  public void addContribution(String language, int count, int commentCount) {
    LanguageCount languageCount = details.computeIfAbsent(language, LanguageCount::new);
    languageCount.incrementCount(count, commentCount);
    total += count;
    totalComment += commentCount;
  }

  public int getTotal() {
    return total;
  }

  public int getTotalComment() {
    return totalComment;
  }

  public Map<String, LanguageCount> getDetails() {
    return details;
  }


  @Override
  public String toString() {
    return "ContributionTypeDetails{" +
            "total=" + total +
            ", details=" + details +
            '}';
  }
}
