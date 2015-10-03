package io.vertx.example.sync.mongo;

import co.paralleluniverse.fibers.Suspendable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.sync.SyncVerticle;

import java.util.Arrays;
import java.util.List;

import static io.vertx.ext.sync.Sync.awaitResult;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends SyncVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  @Suspendable
  public void start() throws Exception {

    JsonObject config = new JsonObject()
      .put("connection_string", "mongodb://localhost:27018")
      .put("db_name", "my_DB");

    // Deploy an embedded mongo database so we can test against that, this can be disabled using embedded-mongo.skip
    if (! Boolean.getBoolean("embedded-mongo.skip")) {
      String deploymentID = awaitResult(h -> vertx.deployVerticle("service:io.vertx.vertx-mongo-embedded-db", h));
      System.out.println("dep id of embedded mongo verticle is " + deploymentID);
    }

    // Create the client
    MongoClient mongo = MongoClient.createShared(vertx, config);

    //Create a collection
    Void v = awaitResult(h -> mongo.createCollection("users", h));

    // Insert some docs:
    List<JsonObject> users = Arrays.asList(new JsonObject().put("username", "temporalfox").put("firstname", "Julien").put("password", "bilto"),
      new JsonObject().put("username", "purplefox").put("firstname", "Tim").put("password", "wibble"));

    for (JsonObject user : users) {
      String id = awaitResult(h -> mongo.insert("users", user, h));
      System.out.println("Inserted id is " + id);
    }

    // Now query them
    List<JsonObject> results = awaitResult(h -> mongo.find("users", new JsonObject(), h));
    System.out.println("Retrieved " + results.size() + " results");

    // Print them
    results.forEach(System.out::println);

  }
}
