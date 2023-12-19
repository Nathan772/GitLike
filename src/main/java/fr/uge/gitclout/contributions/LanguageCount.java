package fr.uge.gitclout.contributions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class LanguageCount {
  @JsonProperty("language")
  private final String language;
  @JsonProperty("count")
  private int count;
  @JsonProperty("commentCount")
  private int commentCount;

  public LanguageCount(String language, int count, int commentCount) {
    this.language = language;
    this.count = count;
    this.commentCount = commentCount;
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

  public void incrementCount() {
    this.count++;
  }

  // Si besoin, ajoutez une méthode pour incrémenter commentCount
  // public void incrementCommentCount() {
  //     this.commentCount++;
  // }

  // Méthodes pour la modification de commentCount si nécessaire
  // public void setCommentCount(int commentCount) {
  //     this.commentCount = commentCount;
  // }
}
