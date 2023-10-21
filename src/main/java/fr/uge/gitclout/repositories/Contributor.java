package fr.uge.gitclout.repositories;

import java.util.HashMap;

public record Contributor(
        String name,
        String email,
        HashMap<FileType, Integer> contributionType,
        HashMap<Language, Integer> contributionLanguage
) {
}
