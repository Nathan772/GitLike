package fr.uge.gitclout.repositories;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Repository(String name, String URL) {
    public Repository{
        Objects.requireNonNull(name, "Your repository name cannot be null");
        Objects.requireNonNull(URL, "Your repository URL cannot be null");
    }


}
