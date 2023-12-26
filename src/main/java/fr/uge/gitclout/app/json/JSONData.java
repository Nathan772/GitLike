package fr.uge.gitclout.app.json;

import fr.uge.gitclout.contribution.AuthorContributions;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public sealed interface JSONData permits JSONData.JSONRepository, JSONData.JSONUrl, JSONData.JSONContributions {

  @Serdeable
  record JSONRepository(
          String name,
          String URL,
          List<String> tags
  ) implements JSONData {
  }

  @Serdeable
  record JSONUrl(
          String URL
  ) implements JSONData {
  }

  @Serdeable
  record JSONContributions(
          String tagName,
          List<AuthorContributions> contributions
  ) implements JSONData {
  }
}
