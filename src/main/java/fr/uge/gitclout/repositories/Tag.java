package fr.uge.gitclout.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Tag(String name, Date date, Repository repository) {
    public Tag{
        Objects.requireNonNull(name, "You cannot have a null for your tag name");
        Objects.requireNonNull(date, "You cannot have a null for the date");
        Objects.requireNonNull(repository, "you cannot hava a null repository");
    }

}
