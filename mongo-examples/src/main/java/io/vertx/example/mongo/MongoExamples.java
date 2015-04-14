package io.vertx.example.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;

public class MongoExamples {
  private MongoService mongoService;
  private JsonObject book = new JsonObject().put("title", "book title");

  /**
   * Creates a new MongoExamples instance.
   * 
   * @param mongoService
   *          A reference to a MongoService (proxy) instance which is used to
   *          perform the interactions with MongoDB.
   */
  public MongoExamples(MongoService mongoService) {
    this.mongoService = mongoService;
  }

  /**
   * Save a document. If the document has no _id field, it is inserted,
   * otherwise, it is upserted. Upserted means it is inserted if it doesn’t
   * already exist, otherwise it is updated. If the document is inserted and has
   * no id, then the id field generated will be returned to the result handler.
   * 
   * @param handler
   */
  public void save(Handler<AsyncResult<String>> handler) {
    this.mongoService.save("books", book, result -> {
      if (result.succeeded()) {
        String id = result.result();
        System.out.println("Saved object with id " + id);
      } else {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * Save a document. Use the returned id to update the document by changing a
   * property and saving it again.
   * 
   * @param handler
   */
  public void saveWithId(Handler<AsyncResult<String>> handler) {
	final JsonObject book = new JsonObject(this.book.toString());
    this.mongoService.save("books", book, result -> {
      if (result.succeeded()) {
        String id = result.result();
        book.put("_id", id).put("title", "new title");
        this.mongoService.save("books", book, handler);
      } else {
        result.cause().printStackTrace();
      }

    });
  }

  /**
   * Insert a document. If the document is inserted and has no id, then the id
   * field generated will be returned to the result handler.
   * 
   * @param handler
   */
  public void insert(Handler<AsyncResult<String>> handler) {
    mongoService.insert("books", book, result -> {
      if (result.succeeded()) {
        String id = result.result();
        System.out.println("Inserted object with id " + id);
      } else {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * @param handler
   */
  public void insertWithId(Handler<AsyncResult<String>> handler) {
    this.mongoService.insert("books", book, result -> {
      if (result.succeeded()) {
        String id = result.result();
        book.put("_id", id).put("title", "new title");
        this.mongoService.insert("books", book, handler);
      } else {
        result.cause().printStackTrace();
      }
    });
  }

}
