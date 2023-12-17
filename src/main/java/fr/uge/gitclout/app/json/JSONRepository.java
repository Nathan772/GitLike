package fr.uge.gitclout.app.json;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record JSONRepository(
        String name,
        String URL,
        List<String> tags
) implements JSONData {
}