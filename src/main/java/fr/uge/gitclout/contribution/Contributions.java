package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;

@Serdeable
public class Contributions {
  @JsonValue
  private final List<AuthorContributions> contributions = new ArrayList<>();

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

  public List<AuthorContributions> getContributions() {
    return contributions;
  }

  @Override
  public String toString() {
    return "Contributions{" +
            "contributions=" + contributions +
            '}';
  }
}