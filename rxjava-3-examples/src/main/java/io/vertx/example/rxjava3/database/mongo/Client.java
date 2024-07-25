package io.vertx.example.rxjava3.database.mongo;

import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.json.JsonObject;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.mongo.MongoClient;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  private MongoClient mongo;

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() throws Exception {

    JsonObject config = new JsonObject()
      .put("connection_string", "mongodb://localhost:27018")
      .put("db_name", "my_DB");

    // Create the client
    mongo = MongoClient.createShared(vertx, config);

    insertAndFind();
  }

  private void insertAndFind() {
    // Documents to insert
    Flowable<JsonObject> documents = Flowable.just(
      new JsonObject().put("username", "temporalfox").put("firstname", "Julien").put("password", "bilto"),
      new JsonObject().put("username", "purplefox").put("firstname", "Tim").put("password", "wibble")
    );

    mongo
      .rxCreateCollection("users")
      .andThen(
        // After collection is created we insert each document
        documents.flatMap(doc -> mongo.rxInsert("users", doc).toFlowable())
      )
      .doOnNext(id -> {
        System.out.println("Inserted document " + id);
      })
      .lastElement()
      .flatMapSingle(id -> {
        // Everything has been inserted now we can query mongo
        System.out.println("Insertions done");
        return mongo.rxFind("users", new JsonObject());
      })
      .subscribe(results -> {
        System.out.println("Results " + results);
      }, error -> {
        System.out.println("Err");
        error.printStackTrace();
      });
  }
}
