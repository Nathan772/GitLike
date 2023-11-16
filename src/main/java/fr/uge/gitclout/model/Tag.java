package fr.uge.gitclout.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

import static jakarta.persistence.GenerationType.AUTO;

@Serdeable
@Entity
@Table(name = "tag")
public class Tag {
  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;
  private String name;
  private String gitName;
  private Date date;
  @ManyToOne
  private Repository repository;

  public Tag(String name, String gitName, Date date, Repository repository) {
    Objects.requireNonNull(name, "You cannot have a null for your tag name");
    Objects.requireNonNull(date, "You cannot have a null for the date");
    Objects.requireNonNull(repository, "you cannot hava a null repository");
    this.name = name;
    this.gitName = gitName;
    this.date = date;
    this.repository = repository;
  }

  public Tag() {

  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getGitName() {
    return gitName;
  }

  public Date getDate() {
    return date;
  }

  public Repository getRepository() {
    return repository;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setGitName(String gitName) {
    this.gitName = gitName;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setRepository(Repository repository) {
    this.repository = repository;
  }
}