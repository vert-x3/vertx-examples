package io.vertx.example.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;

public class MongoExamples {
  private MongoService mongoService;
  private JsonObject book = new JsonObject().put("title","book title");

  /**
   * Creates a new MongoExamples instance.
   * 
   * @param mongoService A reference to a MongoService (proxy) instance which is used to perform the interactions with MongoDB.
   */
  public MongoExamples(MongoService mongoService) {
    this.mongoService = mongoService;
  }

  public void save(Handler<AsyncResult<String>> handler) {
    this.mongoService.save("books", book, result -> {
      if (result.succeeded()) {
        String id = result.result();
        System.out.println("Saved object with id " + id);
        handler.handle(result);
      } else {
        result.cause().printStackTrace();
      }
    });
  }
  
  public void saveWithId(Handler<AsyncResult<String>> handler) {
    book.put("_id", "123244");
    this.mongoService.save("books", book, result -> {
      if (result.succeeded()) {
        String id = result.result();
        handler.handle(result);
      } else {
        result.cause().printStackTrace();
      }
    });
  }


}
