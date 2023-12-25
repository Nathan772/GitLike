package fr.uge.gitclout.Repository;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.GenerationType.AUTO;
/*
@Serdeable
@Entity
@Table(name = "repository")*/
public class Repository {
  /*@Id
  @GeneratedValue(strategy = AUTO)*/
  private Long id;
  private String name;
  private String URL;
  private String localPath;


  public Repository(
          String name,
          String URL,
          String localPath) {
    Objects.requireNonNull(name, "Your repository name cannot be null");
    Objects.requireNonNull(URL, "Your repository URL cannot be null");
    Objects.requireNonNull(localPath, "Your repository local path cannot be null");
    this.name = name;
    this.URL = URL;
    this.localPath = localPath;
  }

  @Override
  public boolean equals(Object obj){
    return obj instanceof Repository repo && repo.name.equals(name) &&
            repo.URL.equals(URL) && localPath.equals(repo.localPath);
  }

  public int hashCode(){
    return name.hashCode()^URL.hashCode()^localPath.hashCode();
  }
  @Override
  public String toString(){
    var bd = new StringBuilder();
    bd.append("Repository Content : \n");bd.append("name : ");
    bd.append(name); bd.append("\n");
    bd.append("URL : "); bd.append(URL);bd.append("\n");
    return bd.toString();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getURL() {
    return URL;
  }

  public String getLocalPath() {
    return localPath;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setURL(String URL) {
    this.URL = URL;
  }

  public void setLocalPath(String localPath) {
    this.localPath = localPath;
  }

}
