package fr.uge.gitclout.model;

import java.util.Objects;

public record Contributes(Contributor contributor, Tag tag) {
  public Contributes {
    Objects.requireNonNull(contributor, "the contribution associated to the tag cannot be null");
    Objects.requireNonNull(tag, "the tag associated to the contribution cannot be null");
  }
}
