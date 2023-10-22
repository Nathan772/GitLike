package fr.uge.gitclout.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Tag(String name, Date date) {
    public Tag(String name, Date date) {
        Objects.requireNonNull(name, "You cannot have a null for your tag name");
        Objects.requireNonNull(date, "You cannot have a null for the date");
        this.name = name;
        this.date = date;
    }

}
