package fr.uge.gitclout.contributions;

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
