package fr.uge.gitclout.repositories;

public record Language(
        String name,
        String extension,
        FileType fileType,
        String commentsRegex // "" if no comments in this language
) {
}