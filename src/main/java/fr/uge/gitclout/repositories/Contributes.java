package fr.uge.gitclout.repositories;

import java.util.Objects;

public record Contributes(Contributor contributor, Tag tag) {
    public Contributes{
        Objects.requireNonNull(contributor, "the contributor associated to the tag cannot be null");
        Objects.requireNonNull(tag, "the tag associated to the contributor cannot be null");
    }
}
