/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.example.spring.worker;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A book JPA entity.
 *
 * @author Thomas Segismont
 */
@Entity
// Book must annotated with @DataObject because it is used as a parameter type in BookAsyncService
@DataObject(generateConverter = true)
public class Book {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String author;

  // Mandatory for JPA entities
  protected Book() {
  }

  public Book(String name, String author) {
    this.name = name;
    this.author = author;
  }

  // Mandatory for data objects
  public Book(JsonObject jsonObject) {
    BookConverter.fromJson(jsonObject, this);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    BookConverter.toJson(this, json);
    return json;
  }

  @Override
  public String toString() {
    return "Book{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", author='" + author + '\'' +
      '}';
  }
}
