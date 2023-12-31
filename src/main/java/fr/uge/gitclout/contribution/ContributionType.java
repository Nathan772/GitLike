package fr.uge.gitclout.contribution;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Types of contributions.
 */
@Serdeable
public enum ContributionType {
  BUILD,
  CODE,
  RESOURCE,
  CONFIG,
  DOCUMENTATION,
  OTHER
}
