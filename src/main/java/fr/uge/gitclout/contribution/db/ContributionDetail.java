package fr.uge.gitclout.contribution.db;

import jakarta.persistence.*;

import java.util.Objects;

import static jakarta.persistence.GenerationType.AUTO;

/**
 * The class representing a contribution detail in the database.
 */
@Entity
@Table(name = "contribution_detail")
public class ContributionDetail {
  @Id
  @GeneratedValue(strategy = AUTO)
  private Long id;

  private String language;
  private int count;
  private int commentCount;

  @ManyToOne
  private Contribution contribution;

  /**
   * Creates a new contribution detail.
   *
   * @param language     The language of the contribution detail.
   * @param count        The count of the contribution detail.
   * @param commentCount The comment count of the contribution detail.
   * @param contribution The contribution of the contribution detail.
   */
  public ContributionDetail(String language, int count, int commentCount, Contribution contribution) {
    Objects.requireNonNull(language);
    Objects.requireNonNull(contribution);
    this.language = language;
    this.count = count;
    this.commentCount = commentCount;
    this.contribution = contribution;
  }

  /**
   * Creates a new contribution detail.
   * Method protected for Hibernate.
   */
  protected ContributionDetail() {
  }

  /**
   * Returns the id of the contribution detail.
   *
   * @return The id of the contribution detail.
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the language of the contribution detail.
   *
   * @return The language of the contribution detail.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Returns the count of the contribution detail.
   *
   * @return The count of the contribution detail.
   */
  public int getCount() {
    return count;
  }

  /**
   * Returns the comment count of the contribution detail.
   *
   * @return The comment count of the contribution detail.
   */
  public int getCommentCount() {
    return commentCount;
  }

  /**
   * Returns the contribution of the contribution detail.
   *
   * @return The contribution of the contribution detail.
   */
  public Contribution getContribution() {
    return contribution;
  }

  /**
   * Sets the language of the contribution detail.
   *
   * @param language The language of the contribution detail.
   */
  public void setLanguage(String language) {
    Objects.requireNonNull(language);
    this.language = language;
  }

  /**
   * Sets the count of the contribution detail.
   *
   * @param count The count of the contribution detail.
   */
  public void setCount(int count) {
    this.count = count;
  }

  /**
   * Sets the comment count of the contribution detail.
   *
   * @param commentCount The comment count of the contribution detail.
   */
  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }

  /**
   * Sets the contribution of the contribution detail.
   *
   * @param contribution The contribution of the contribution detail.
   */
  public void setContribution(Contribution contribution) {
    Objects.requireNonNull(contribution);
    this.contribution = contribution;
  }
}
