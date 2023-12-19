package fr.uge.gitclout.contributions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.HashMap;
import java.util.Map;

@Serdeable
public class ContributionInfos {
  @JsonProperty("languages")
  Map<String, LanguageCount> languages = new HashMap<>();

  @JsonProperty("totalCount")
  int totalCount;

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

  public boolean isLanguagePresent(String language) {
    return languages.containsKey(language);
  }
}
