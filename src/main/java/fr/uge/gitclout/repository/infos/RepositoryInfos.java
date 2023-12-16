package fr.uge.gitclout.repository.infos;

import fr.uge.gitclout.app.json.JSONData;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record RepositoryInfos(
        String name,
        String URL,
        List<String> tags
) implements JSONData {
}