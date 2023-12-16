package fr.uge.gitclout.repository.infos;

import fr.uge.gitclout.app.json.JSONData;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record URL(
        String URL
) implements JSONData {
}