package fr.uge.gitclout.model;

import java.util.Objects;

public record Contribution(ContributionType type, int linesCount, Language language) {
  public Contribution {
    Objects.requireNonNull(type, "contribution's type cannot be null");
    if (linesCount < 0) {
      throw new IllegalArgumentException("lines count cannot be negative");
    }
    // Language can be null
  }
}
