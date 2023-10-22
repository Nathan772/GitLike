package fr.uge.gitclout.repositories;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record Repository(String name, String URL, List<Tag> tags) {
    public Repository{
        Objects.requireNonNull(name, "Your repository name cannot be null");
        Objects.requireNonNull(URL, "Your repository URL cannot be null");
        Objects.requireNonNull(tags, "Your tag list cannot be null");
    }


    @Override
    /**
     * this method enables to retrieve the tags associated to the repository
     * @return
     * return the list of tags
     */
    public List<Tag> tags(){
        return tags.stream().collect(Collectors.toUnmodifiableList());
    }

}
