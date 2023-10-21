package fr.uge.gitclout.repositories;

import java.util.List;

public record Repository(
        String name,
        String URL,
        List<Tag> tags
) {

}
