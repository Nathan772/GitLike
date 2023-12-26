package fr.uge.gitclout.contribution;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Language(
        String name
) {

  @Override
  public String toString() {
    return name;
  }
}
