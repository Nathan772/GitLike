package fr.uge.gitclout.repositories;

import java.util.Objects;

public record Contributes(Contributor contributor, Tag tag, MyFile file, int commentLines, int lines) {
    public Contributes{
        Objects.requireNonNull(contributor, "the contribution associated to the tag cannot be null");
        Objects.requireNonNull(tag, "the tag associated to the contribution cannot be null");
        Objects.requireNonNull(file, "the file associated to the contribution cannot be null");
        if(commentLines < 0){
            throw new IllegalArgumentException("the number of comment line cannot be lower than 0 ");
        }
        if(lines < 0){
            throw new IllegalArgumentException("the number of line cannot be lower than 0 ");
        }
    }
}
