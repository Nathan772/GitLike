package fr.uge.gitclout.repositories;

import java.util.Objects;

public record Language(String name, String extension, FileType fileType, String commentsRegex// "" if no comments in this language
) {
    public Language{
        Objects.requireNonNull(name, "language's name cannot be null");
        Objects.requireNonNull(extension, "language's extension cannot be null");
        Objects.requireNonNull(commentsRegex, "language's commentRegex cannot be null");
        Objects.requireNonNull(fileType, "language's file type cannot be null");
    }
}