package io.vertx.examples.webapiservice.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;

/**
 * Data object that represents a persistence
 *
 * @author slinkydeveloper
 */
@DataObject
@JsonGen
public class Transaction {

  private String id;
  private String message;
  private String from;
  private String to;
  private Double value;

  public Transaction (
    String id,
    String message,
    String from,
    String to,
    Double value
  ) {
    this.id = id;
    this.message = message;
    this.from = from;
    this.to = to;
    this.value = value;
  }

  public Transaction(JsonObject json) {
    TransactionConverter.fromJson(json, this);
  }

  public Transaction(Transaction other) {
    this.id = other.getId();
    this.message = other.getMessage();
    this.from = other.getFrom();
    this.to = other.getTo();
    this.value = other.getValue();
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    TransactionConverter.toJson(this, json);
    return json;
  }

  @Fluent public Transaction setId(String id){
    this.id = id;
    return this;
  }
  public String getId() {
    return this.id;
  }

  @Fluent public Transaction setMessage(String message){
    this.message = message;
    return this;
  }
  public String getMessage() {
    return this.message;
  }

  @Fluent public Transaction setFrom(String from){
    this.from = from;
    return this;
  }
  public String getFrom() {
    return this.from;
  }

  @Fluent public Transaction setTo(String to){
    this.to = to;
    return this;
  }
  public String getTo() {
    return this.to;
  }

  @Fluent public Transaction setValue(Double value){
    this.value = value;
    return this;
  }
  public Double getValue() {
    return this.value;
  }

}
