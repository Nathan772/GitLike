package fr.uge.gitclout.contributions;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record ContributionsPerAuthor(
        String author,
        List<Contribution> contributions
) {
}
