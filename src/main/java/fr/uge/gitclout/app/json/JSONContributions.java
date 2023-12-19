package fr.uge.gitclout.app.json;

import fr.uge.gitclout.contributions.ContributionInfos;
import fr.uge.gitclout.contributions.ContributionType;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Map;

@Serdeable
public record JSONContributions(
        Map<String, Map<ContributionType, ContributionInfos>> contributions
) implements JSONData {
}
