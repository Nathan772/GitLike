package fr.uge.gitclout.model;

import Language.Language;

import java.util.Objects;

public record MyFile(String name, Language language, Repository repository) {
    public MyFile{
        Objects.requireNonNull(name, "file's name cannot be null");
        Objects.requireNonNull(language, "file's language cannot be null");
        Objects.requireNonNull(repository, "the repository associated to the file cannot be null");
    }
}

