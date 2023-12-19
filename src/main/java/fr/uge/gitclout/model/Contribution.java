package fr.uge.gitclout.model;

import Language.Language;

import java.util.Objects;

public record Contribution(ContributionType type, int linesCount, Language language, Contributor contributor, Tag tag) {
  public Contribution {
    Objects.requireNonNull(type, "contribution's type cannot be null");
    Objects.requireNonNull(contributor, "contributor for a contribution cannot be null");
    Objects.requireNonNull(tag, "the tag used for a contribution cannot be null");
    if (linesCount < 0) {
      throw new IllegalArgumentException("lines count cannot be negative");
    }
    // Language can be null
  }
}
