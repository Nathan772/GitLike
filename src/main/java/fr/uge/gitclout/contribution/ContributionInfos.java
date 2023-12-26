package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.Map;

@Serdeable
public class ContributionInfos {
  @JsonValue
  private Map<String, LanguageCount> languages = new HashMap<>();

  @JsonProperty("totalCount")
  private int totalCount;

  public ContributionInfos() {
  }

  @JsonCreator
  public ContributionInfos(Map<String, LanguageCount> languages, int totalCount) {
    this.languages = languages;
    this.totalCount = totalCount;
  }

  public Map<String, LanguageCount> getLanguages() {
    return languages;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void addLanguage(String language, LanguageCount languageCount) {
    languages.put(language, languageCount);
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public void addTotalCount(int totalCount) {
    this.totalCount += totalCount;
  }

  /*public void incrementLanguageCount(String language, int increment, int commentIncrement) {
    languages.computeIfAbsent(language, k -> new LanguageCount(language, 0, 0))
            .incrementCount(increment, commentIncrement);
  }*/

  public boolean isLanguagePresent(String language) {
    return languages.containsKey(language);
  }

  @Override
  public String toString() {
    return "ContributionInfos{" +
            "languages=" + languages +
            ", totalCount=" + totalCount +
            '}';
  }
}
