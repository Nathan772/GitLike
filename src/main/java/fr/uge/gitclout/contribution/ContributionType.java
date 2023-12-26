package fr.uge.gitclout.contribution;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum ContributionType {
  BUILD,
  CODE,
  RESOURCE,
  CONFIG,
  DOCUMENTATION,
  OTHER
}
