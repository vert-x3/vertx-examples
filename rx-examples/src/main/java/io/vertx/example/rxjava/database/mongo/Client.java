package io.vertx.example.rxjava.database.mongo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.mongo.MongoService;
import rx.Observable;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject()
        .put("connection_string", "mongodb://localhost:27018")
        .put("db_name", "my_DB"));

    vertx.deployVerticle("service:io.vertx.mongo-embedded-db", db -> {
      if (db.succeeded()) {
        vertx.deployVerticle("service:io.vertx.mongo-service", options, service -> {
          if (service.succeeded()) {

            // Create the service proxy
            MongoService mongoService = MongoService.createEventBusProxy(vertx, "vertx.mongo");

            // Documents to insert
            Observable<JsonObject> documents = Observable.just(
                new JsonObject().put("username", "temporalfox").put("firstname", "Julien").put("password", "bilto"),
                new JsonObject().put("username", "purplefox").put("firstname", "Tim").put("password", "wibble")
            );

            //
            mongoService.
                createCollectionObservable("users").

                // After collection is created we insert each document
                flatMap(v -> documents.flatMap(
                        doc -> mongoService.insertObservable("users", doc))
                ).subscribe(

                id -> {
                  System.out.println("Inserted document " + id);
                }, error -> {
                  System.out.println("Err");
                  error.printStackTrace();
                }, () -> {

                  // Everything has been inserted now we can query mongo
                  System.out.println("Insertions done");
                  mongoService.findObservable("users", new JsonObject()).
                      subscribe(results -> {
                        System.out.println("Results " + results);
                      });
                });

          } else {
            System.out.println("Could not start mongo service");
            db.cause().printStackTrace();
          }
        });
      } else {
        System.out.println("Could not start mongo embedded");
        db.cause().printStackTrace();
      }
    });
  }
}
