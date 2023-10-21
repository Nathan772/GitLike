package fr.uge.gitclout.repositories;

import java.util.Date;
import java.util.List;

public record Tag(
        String name,
        Date date,
        List<Contributor> contributors
) {


}
