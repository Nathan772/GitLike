package fr.uge.gitclout.model;

import java.util.Date;
import java.util.Objects;

public record Tag(String name, Date date, Repository repository) {
  public Tag {
    Objects.requireNonNull(name, "You cannot have a null for your tag name");
    Objects.requireNonNull(date, "You cannot have a null for the date");
    Objects.requireNonNull(repository, "you cannot hava a null repository");
  }

}