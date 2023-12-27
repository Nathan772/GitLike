package fr.uge.gitclout.app.json;

import fr.uge.gitclout.Contributor.Contributor;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import java.util.Map;

/*
@Serdeable
public record JSONContributors(List<String> contributors, String tagName, String repositoryName) implements JSONData{
}*/

@Serdeable
public record JSONContributors(List<Contributor> contributors, String tagName, String repositoryName) implements JSONData{

}
