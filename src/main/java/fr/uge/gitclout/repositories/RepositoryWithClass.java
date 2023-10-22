package fr.uge.gitclout.repositories;

import java.util.Objects;

/**
 * this records enable to create unmutable tuples of repository with tag
 */
public record RepositoryWithClass(Repository repository, Tag tag) {
    public RepositoryWithClass{
        Objects.requireNonNull(repository, "the repository with your tag cannot be null");
        Objects.requireNonNull(tag, "the tag with your repository cannot be null");
    }
}
