package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Serdeable
public class AuthorContributions {
  @JsonProperty("author")
  private final String author;
  @JsonProperty("contributionsByType")
  private final Map<ContributionType, ContributionTypeDetails> contributionsByType = new HashMap<>();

  public AuthorContributions() {
    this.author = "";
  }

  @JsonCreator
  public AuthorContributions(@JsonProperty("author") String author,
                             @JsonProperty("contributionsByType") Map<ContributionType, ContributionTypeDetails> contributionsByType) {
    Objects.requireNonNull(author);
    Objects.requireNonNull(contributionsByType);
    this.author = author;
    this.contributionsByType.putAll(contributionsByType);
  }

  public AuthorContributions(String author) {
    Objects.requireNonNull(author);
    this.author = author;
  }

  public void addContribution(ContributionType type, String language, int count, int commentCount) {
    Objects.requireNonNull(type);
    if(count < 0) throw new IllegalArgumentException("count cannot be lesser than 0");
    if(commentCount < 0) throw new IllegalArgumentException("comment count cannot be lesser than 0");
    Objects.requireNonNull(language);

    ContributionTypeDetails typeDetails = contributionsByType.computeIfAbsent(type, k -> new ContributionTypeDetails());
    typeDetails.addContribution(language, count, commentCount);
  }

  public String getAuthor() {
    return author;
  }

  public Map<ContributionType, ContributionTypeDetails> getContributionsByType() {
    return contributionsByType;
  }

  @Override
  public String toString() {
    return "AuthorContributions{" +
            "author='" + author + '\'' +
            ", contributionsByType=" + contributionsByType +
            '}';
  }
}
