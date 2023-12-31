package fr.uge.gitclout.app;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
        info = @Info(
                title = "db",
                version = "0.0"
        )
)

public class Application {

  /**
   * Starts the Micronaut application.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    Micronaut.run(Application.class, args);
  }
}