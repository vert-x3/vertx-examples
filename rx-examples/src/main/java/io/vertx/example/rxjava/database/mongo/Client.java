package io.vertx.example.rxjava.database.mongo;

import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.mongo.MongoClient;
import rx.Observable;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  private MongoClient mongo;

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    JsonObject config = new JsonObject()
        .put("connection_string", "mongodb://localhost:27018")
        .put("db_name", "my_DB");

    // Deploy an embedded mongo database so we can test against that
    vertx.deployVerticle("service:io.vertx.vertx-mongo-embedded-db", db -> {
      if (db.succeeded()) {

        // Create the client
        mongo = MongoClient.createShared(vertx, config);

        insertAndFind();

      } else {
        System.out.println("Could not start mongo embedded");
        db.cause().printStackTrace();
      }
    });
  }

  private void insertAndFind() {
    // Documents to insert
    Observable<JsonObject> documents = Observable.just(
      new JsonObject().put("username", "temporalfox").put("firstname", "Julien").put("password", "bilto"),
      new JsonObject().put("username", "purplefox").put("firstname", "Tim").put("password", "wibble")
    );

    mongo.rxCreateCollection("users").flatMapObservable(v -> {
      // After collection is created we insert each document
      return documents.flatMap(doc -> mongo.rxInsert("users", doc).toObservable());
    }).doOnNext(id -> {
      System.out.println("Inserted document " + id);
    }).last().toSingle().flatMap(id -> {
      // Everything has been inserted now we can query mongo
      System.out.println("Insertions done");
      return mongo.rxFind("users", new JsonObject());
    }).subscribe(results -> {
      System.out.println("Results " + results);
    }, error -> {
      System.out.println("Err");
      error.printStackTrace();
    });
  }
}
