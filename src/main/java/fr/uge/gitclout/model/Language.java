package fr.uge.gitclout.model;

import java.util.Objects;

public record Language(String name, String extension, ContributionType fileType, String commentsRegex) {
  public Language {
    Objects.requireNonNull(name, "language's name cannot be null");
    Objects.requireNonNull(extension, "language's extension cannot be null");
    Objects.requireNonNull(commentsRegex, "language's commentRegex cannot be null");
    Objects.requireNonNull(fileType, "language's file type cannot be null");
  }
}