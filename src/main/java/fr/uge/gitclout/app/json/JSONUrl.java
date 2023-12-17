package fr.uge.gitclout.app.json;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record JSONUrl(
        String URL
) implements JSONData {
}