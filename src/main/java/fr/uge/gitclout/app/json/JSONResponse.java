package fr.uge.gitclout.app.json;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record JSONResponse(
        String message,
        String status,
        JSONData data
) {
}
