package fr.uge.gitclout.contribution.db;

import fr.uge.gitclout.repository.db.Repository;
import fr.uge.gitclout.tag.db.Tag;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.GenerationType.AUTO;

/**
 * The class representing a contribution in the database.
 */
@Serdeable
@Entity
@Table(name = "contribution")
public class Contribution {
  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;
  private String author;
  private String contributionType;
  private int total;
  private int totalComment;
  @ManyToOne
  private Repository repository;
  @ManyToOne
  private Tag tag;
  @OneToMany(mappedBy = "contribution", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ContributionDetail> contributionDetails;

  /**
   * Creates a new contribution.
   *
   * @param author           The author of the contribution.
   * @param contributionType The type of the contribution.
   * @param total            The total of the contribution.
   * @param totalComment     The total of the comments of the contribution.
   * @param repository       The repository of the contribution.
   * @param tag              The tag of the contribution.
   */
  public Contribution(String author, String contributionType, int total, int totalComment, Repository repository, Tag tag) {
    Objects.requireNonNull(author);
    Objects.requireNonNull(contributionType);
    Objects.requireNonNull(repository);
    Objects.requireNonNull(tag);
    this.author = author;
    this.contributionType = contributionType;
    this.total = total;
    this.totalComment = totalComment;
    this.repository = repository;
    this.tag = tag;
  }

  /**
   * Creates a new contribution.
   * Method protected for Hibernate.
   */
  protected Contribution() {
  }

  /**
   * Returns the id of the contribution.
   *
   * @return The id of the contribution.
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the author of the contribution.
   *
   * @return The author of the contribution.
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Returns the type of the contribution.
   *
   * @return The type of the contribution.
   */
  public String getContributionType() {
    return contributionType;
  }

  /**
   * Returns the total of the contribution.
   *
   * @return The total of the contribution.
   */
  public int getTotal() {
    return total;
  }

  /**
   * Returns the total of the comments of the contribution.
   *
   * @return The total of the comments of the contribution.
   */
  public int getTotalComment() {
    return totalComment;
  }

  /**
   * Returns the repository of the contribution.
   *
   * @return The repository of the contribution.
   */
  public Repository getRepository() {
    return repository;
  }

  /**
   * Returns the tag of the contribution.
   *
   * @return The tag of the contribution.
   */
  public Tag getTag() {
    return tag;
  }

  /**
   * Sets the author of the contribution.
   */
  public void setAuthor(String author) {
    Objects.requireNonNull(author);
    this.author = author;
  }

  /**
   * Sets the type of the contribution.
   */
  public void setContributionType(String contributionType) {
    Objects.requireNonNull(contributionType);
    this.contributionType = contributionType;
  }

  /**
   * Sets the total of the contribution.
   */
  public void setTotal(int total) {
    this.total = total;
  }

  /**
   * Sets the total of the comments of the contribution.
   */
  public void setTotalComment(int totalComment) {
    this.totalComment = totalComment;
  }

  /**
   * Sets the repository of the contribution.
   */
  public void setRepository(Repository repository) {
    Objects.requireNonNull(repository);
    this.repository = repository;
  }

  /**
   * Sets the tag of the contribution.
   */
  public void setTag(Tag tag) {
    Objects.requireNonNull(tag);
    this.tag = tag;
  }
}
