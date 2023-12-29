package fr.uge.gitclout.contribution;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public record Language(String name) {
  public Language(String name){
    Objects.requireNonNull(name);
    this.name = name;
  }
  @Override
  public String toString() {
    return name;
  }
}
