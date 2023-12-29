package fr.uge.gitclout.app.json;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public record JSONResponse(
        String message,
        String status,
        JSONData data
) {
    public JSONResponse{
        Objects.requireNonNull(message);
        Objects.requireNonNull(status);
        Objects.requireNonNull(data);
    }
}
