package fr.uge.gitclout.Contributor;

import java.util.Objects;

public class Contributor {
  private final String name;
  private final String email;
  public Contributor(String name, String email) {
    Objects.requireNonNull(name, "contributor's name cannot be null");
    Objects.requireNonNull(email, "contributor's email cannot be null");

    this.name = name;
    this.email = email;
  }

  public String toString(){
    var bd = new StringBuilder();
    bd.append("contributor's name : ");
    bd.append(name);
    bd.append("\n");
    bd.append("contributor's email : ");
    bd.append(email);
    bd.append("\n");
    return bd.toString();
  }

  public int hashCode(){
    return name.hashCode()^email.hashCode();
  }

  public boolean equals(Object obj){
    return obj instanceof Contributor contributor && contributor.email.equals(email) &&
            contributor.name.equals(name);
  }
  public String email(){
    return this.email;
  }

  public String name(){
    return this.name;
  }
}
