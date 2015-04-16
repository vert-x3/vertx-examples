package io.vertx.example.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;

import java.util.List;

/**
 * 
 * 
 * @author Erwin de Gier
 *
 */
public class MongoExamples {
  private MongoService mongoService;
  private JsonObject book = new JsonObject().put("title", "book title").put("edition", "first");
  private JsonObject anotherBook = new JsonObject().put("title", "book title2").put("edition", "second");

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
   * Insert an existing document, this fails.
   * 
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

  /**
   * replace an existing document.
   * 
   * @param handler
   */
  public void replace(Handler<AsyncResult<Void>> handler) {
    mongoService.replace("books", book, anotherBook, result -> {
      if (result.succeeded()) {
        System.out.println("Book replaced !");
      } else {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * Find all documents matching the query.
   * 
   * @param handler
   */
  public void find(Handler<AsyncResult<List<JsonObject>>> handler) {
    this.mongoService.find("books", new JsonObject(), result -> {
      if (result.succeeded()) {
        for (JsonObject json : result.result()) {
          System.out.println(json.encodePrettily());
        }
      } else {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * Return the first found document. Only return the field "title".
   * 
   * @param handler
   */
  public void findOne(Handler<AsyncResult<JsonObject>> handler) {
    this.mongoService.findOne("books", new JsonObject(), new JsonObject().put("title", true), result -> {
      if (result.succeeded()) {
        System.out.println(result.result().encodePrettily());
      } else {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * Remove all documents matching the query.
   * 
   * @param handler
   */
  public void remove(Handler<AsyncResult<Void>> handler) {
    this.mongoService.remove("books", new JsonObject(), result -> {
      if (!result.succeeded()) {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * Remove the first found document. Only return the field "title".
   * 
   * @param handler
   */
  public void removeOne(Handler<AsyncResult<Void>> handler) {
    this.mongoService.removeOne("books", new JsonObject(), result -> {
      if (!result.succeeded()) {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * Count all documents matching the query.
   * 
   * @param handler
   */
  public void count(Handler<AsyncResult<Long>> handler) {
    this.mongoService.count("books", new JsonObject(), result -> {
      if (result.succeeded()) {
        System.out.println("Found number of books: " + result.result());
      } else {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }

  /**
   * Create a new collection
   * 
   * @param handler
   */
  public void createCollection(Handler<AsyncResult<Void>> handler) {
    mongoService.createCollection("mynewcollection", result -> {
      if (!result.succeeded()) {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }
  
  /**
   * List all collections in this database
   * 
   * @param handler
   */
  public void getCollections(Handler<AsyncResult<List<String>>> handler) {
    mongoService.getCollections(result -> {
      if (!result.succeeded()) {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }
}
