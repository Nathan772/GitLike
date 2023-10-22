package fr.uge.gitclout.repositories;

import java.util.Objects;

public record File(String name, Language language, Repository repository) {
    public File{
        Objects.requireNonNull(name, "file's name cannot be null");
        Objects.requireNonNull(language, "file's language cannot be null");
        Objects.requireNonNull(repository, "the repository associated to the file cannot be null");
    }
}
