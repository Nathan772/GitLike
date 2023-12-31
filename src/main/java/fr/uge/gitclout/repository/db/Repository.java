package fr.uge.gitclout.repository.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.uge.gitclout.tag.db.Tag;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

/**
 * The class representing a repository in the database.
 */
@Serdeable
@Entity
@Table(name = "repository")
public class Repository {
  @Id
  private String repositoryURL;
  private String repositoryName;
  @JsonIgnore
  private String repositoryLocalPath;
  @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Tag> tags;

  /**
   * Creates a new repository.
   *
   * @param repositoryURL       The URL of the repository.
   * @param repositoryName      The name of the repository.
   * @param repositoryLocalPath The local path of the repository.
   */
  public Repository(String repositoryURL, String repositoryName, String repositoryLocalPath) {
    Objects.requireNonNull(repositoryURL, "You cannot have a null for your repository URL");
    Objects.requireNonNull(repositoryName, "You cannot have a null for your repository name");
    Objects.requireNonNull(repositoryLocalPath, "You cannot have a null for your repository local path");
    this.repositoryURL = repositoryURL;
    this.repositoryName = repositoryName;
    this.repositoryLocalPath = repositoryLocalPath;
  }

  /**
   * Creates a new repository.
   * Method protected for Hibernate.
   */
  protected Repository() {
  }

  /**
   * Returns the URL of the repository.
   *
   * @return The URL of the repository.
   */
  public String getRepositoryURL() {
    return repositoryURL;
  }

  /**
   * Returns the name of the repository.
   *
   * @return The name of the repository.
   */
  public String getRepositoryName() {
    return repositoryName;
  }

  /**
   * Returns the local path of the repository.
   *
   * @return The local path of the repository.
   */
  public String getRepositoryLocalPath() {
    return repositoryLocalPath;
  }

  /**
   * Sets the URL of the repository.
   *
   * @param repositoryURL The URL of the repository.
   */
  public void setRepositoryURL(String repositoryURL) {
    Objects.requireNonNull(repositoryURL);
    this.repositoryURL = repositoryURL;
  }

  /**
   * Sets the name of the repository.
   *
   * @param repositoryName The name of the repository.
   */
  public void setRepositoryName(String repositoryName) {
    Objects.requireNonNull(repositoryName);
    this.repositoryName = repositoryName;
  }

  /**
   * Sets the local path of the repository.
   *
   * @param repositoryLocalPath The local path of the repository.
   */
  public void setRepositoryLocalPath(String repositoryLocalPath) {
    Objects.requireNonNull(repositoryLocalPath);
    this.repositoryLocalPath = repositoryLocalPath;
  }
}
