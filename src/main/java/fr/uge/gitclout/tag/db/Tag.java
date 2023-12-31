package fr.uge.gitclout.tag.db;

import fr.uge.gitclout.contribution.db.Contribution;
import fr.uge.gitclout.repository.db.Repository;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.GenerationType.AUTO;

/**
 * This class represents a tag in the database.
 */
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
  @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Contribution> contributions;

  public Tag(String tagName, String refTagName, Repository repository) {
    Objects.requireNonNull(tagName, "you cannot have a null tagName");
    Objects.requireNonNull(repository, "you cannot hava a null repository");
    Objects.requireNonNull(refTagName, "you cannot have a null refTagName");
    this.tagName = tagName;
    this.refTagName = refTagName;
    this.repository = repository;
  }

  /**
   * This constructor is used by Hibernate.
   */
  protected Tag() {
  }

  /**
   * This method returns the id of the tag.
   *
   * @return the id of the tag.
   */
  public Long getId() {
    return id;
  }

  /**
   * This method returns the name of the tag.
   *
   * @return the name of the tag.
   */
  public String getTagName() {
    return tagName;
  }

  /**
   * This method returns the name of the ref tag.
   *
   * @return the name of the ref tag.
   */
  public String getRefTagName() {
    return refTagName;
  }

  /**
   * This method returns the repository of the tag.
   *
   * @return the repository of the tag.
   */
  public Repository getRepository() {
    return repository;
  }

  /**
   * This method sets the name of the tag.
   *
   * @param tagName the name of the tag.
   */
  public void setTagName(String tagName) {
    Objects.requireNonNull(tagName);
    this.tagName = tagName;
  }

  /**
   * This method sets the name of the ref tag.
   *
   * @param refTagName the name of the ref tag.
   */
  public void setRefTagName(String refTagName) {
    Objects.requireNonNull(refTagName);
    this.refTagName = refTagName;
  }

  /**
   * This method sets the repository of the tag.
   *
   * @param repository the repository of the tag.
   */
  public void setRepository(Repository repository) {
    Objects.requireNonNull(repository);
    this.repository = repository;
  }
}
