package fr.uge.gitclout.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Serdeable
@Entity
@Table(name = "repository")
public class Repository {
  @Id
  private String repositoryURL;
  private String repositoryName;
  @JsonIgnore
  private String repositoryLocalPath;

  public Repository(String repositoryURL, String repositoryName, String repositoryLocalPath) {
    Objects.requireNonNull(repositoryURL, "You cannot have a null for your repository URL");
    Objects.requireNonNull(repositoryName, "You cannot have a null for your repository name");
    Objects.requireNonNull(repositoryLocalPath, "You cannot have a null for your repository local path");
    this.repositoryURL = repositoryURL;
    this.repositoryName = repositoryName;
    this.repositoryLocalPath = repositoryLocalPath;
  }

  public Repository() {
  }

  public String getRepositoryURL() {
    return repositoryURL;
  }

  public String getRepositoryName() {
    return repositoryName;
  }

  public String getRepositoryLocalPath() {
    return repositoryLocalPath;
  }

  public void setRepositoryURL(String repositoryURL) {
    this.repositoryURL = repositoryURL;
  }

  public void setRepositoryName(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  public void setRepositoryLocalPath(String repositoryLocalPath) {
    this.repositoryLocalPath = repositoryLocalPath;
  }
}
