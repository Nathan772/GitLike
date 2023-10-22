package fr.uge.gitclout.repositories;

import java.util.HashMap;
import java.util.Objects;

public record Contributor(
        String name,
        String email,
        HashMap<FileType, Integer> contributionType,
        HashMap<Language, Integer> contributionLanguage
) {
    public Contributor{
        Objects.requireNonNull(name, "contributor's name cannot be null");
        Objects.requireNonNull(email,  "contributor's email cannot be null");

    }
}
