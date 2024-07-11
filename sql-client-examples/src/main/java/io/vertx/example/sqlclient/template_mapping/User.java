package io.vertx.example.sqlclient.template_mapping;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;
import io.vertx.sqlclient.templates.annotations.RowMapped;

/**
 * Vert.x data object generating a SQL templates mapper
 */
@DataObject
@JsonGen
@RowMapped(formatter = SnakeCase.class)
@ParametersMapped(formatter = SnakeCase.class)
public class User {

  private int id;
  private String firstName;
  private String lastName;

  public User(JsonObject json) {
    UserConverter.fromJson(json, this);
  }

  public User() {
  }

  public int getId() {
    return id;
  }

  public User setId(int id) {
    this.id = id;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      '}';
  }
}
