package fr.uge.gitclout.app.json;

import fr.uge.gitclout.Contribution.ContributionIntermediate;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record JSONContribution(List<ContributionIntermediate> contributions, String contributorEmail, String contributorName,
                               String tag) implements JSONData{
}