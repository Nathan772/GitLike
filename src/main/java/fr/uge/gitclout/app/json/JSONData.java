package fr.uge.gitclout.app.json;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public sealed interface JSONData permits JSONRepository, JSONUrl, JSONContributions {
}
