package fr.uge.gitclout.Contributor;

import fr.uge.gitclout.repository.Repository;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Comparator;
import java.util.Objects;
@Serdeable
public class Contributor implements Comparable<Contributor> {
  private String name;
  private String email;
  public Contributor(String name, String email) {
    Objects.requireNonNull(name, "contributor's name cannot be null");
    Objects.requireNonNull(email, "contributor's email cannot be null");

    this.name = name;
    this.email = email;
  }
  @Override
  public int compareTo(Contributor other){
    return name.compareTo(other.name);
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
    //a particular case is handle in a particular way
    return obj instanceof Contributor contributor && contributor.email.replace("+"," ").equals(email.replace("+"," ")) &&
            contributor.name.replace("+"," ").equals(name.replace("+"," "));
  }
  /**

  an accessor to the field email
   @return
   the email of the contributor
   */
  public String email(){
    return this.email;
  }

  /**

   an accessor to the field name
   @return
   the name of the contributor
   */

  public String name(){
    return this.name;
  }





  //getters and setters necessary to use a type as a new type recognized with

  /**
   * a getter to the field email
   * @return
   * access to the field email
   */
  public String getEmail() {
    return email;
  }
  /**
   * a getter to the field Name
   * @return
   * access to the field name
   */
  public String getName() {
    return name;
  }

  /**
   * a setter to the field name
   * @param name
   * the new name of the contributor
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * a setter to the field mail
   * @param email
   * the new mail of the contributor
   */
  public void setEmail(String email) {
    this.email = email;
  }

}
