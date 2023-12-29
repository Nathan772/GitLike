package fr.uge.gitclout.tag;

import fr.uge.gitclout.repository.Repository;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.Objects;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "tag")
public class Tag {
  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;
  private String tagName;
  private String refTagName;
  @ManyToOne
  private Repository repository;

  public Tag(String name, String gitName, Repository repository) {
    Objects.requireNonNull(name, "You cannot have a null for your tag name");
    Objects.requireNonNull(repository, "you cannot hava a null repository");
    Objects.requireNonNull(gitName, "you cannot have a null gitName");
    this.tagName = name;
    this.refTagName = gitName;
    this.repository = repository;
  }

  public Tag() {

  }

  public Long getId() {
    return id;
  }

  public String getTagName() {
    return tagName;
  }

  public String getRefTagName() {
    return refTagName;
  }

  public Repository getRepository() {
    return repository;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setTagName(String name) {
    this.tagName = name;
  }

  public void setRefTagName(String gitName) {
    this.refTagName = gitName;
  }

  public void setRepository(Repository repository) {
    this.repository = repository;
  }
}