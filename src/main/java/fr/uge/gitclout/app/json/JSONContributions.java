package fr.uge.gitclout.app.json;

import fr.uge.gitclout.contributions.ContributionsPerAuthor;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record JSONContributions(
        List<ContributionsPerAuthor> contributions
) implements JSONData {
}
