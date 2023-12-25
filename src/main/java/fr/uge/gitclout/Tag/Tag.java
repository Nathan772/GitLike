package fr.uge.gitclout.Tag;
import fr.uge.gitclout.Repository.Repository;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

import static jakarta.persistence.GenerationType.AUTO;

/*@Serdeable
@Entity
@Table(name = "tag")*/
public class Tag {
 /* @Id
  @GeneratedValue(strategy = AUTO)*/
  private Long id;
  private String name;
  private String gitName;
  private Date date;
  //@ManyToOne
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

  @Override
  public boolean equals(Object obj){
    return obj instanceof Tag tag1 && tag1.name.equals(name) && tag1.gitName.equals(gitName) && tag1.date.equals(date)
            && tag1.repository.equals(repository);
  }
  @Override
  public int hashCode(){
    return name.hashCode()^gitName.hashCode()^date.hashCode()^repository.hashCode();
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String toString() {
    var bd = new StringBuilder();
    bd.append("Tag infos : \n "); bd.append(name);
    bd.append("\n Repository : \n");bd.append(repository);
    bd.append("\n Date : ");bd.append(date);
    bd.append("\n");
    return bd.toString();
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