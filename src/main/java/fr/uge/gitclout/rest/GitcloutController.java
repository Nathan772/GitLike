package fr.uge.gitclout.rest;

import io.micronaut.http.annotation.*;

@Controller("/api")
public class GitcloutController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}