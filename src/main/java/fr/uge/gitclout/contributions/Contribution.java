package fr.uge.gitclout.contributions;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Contribution(
        String language,
        int count
) {

}
