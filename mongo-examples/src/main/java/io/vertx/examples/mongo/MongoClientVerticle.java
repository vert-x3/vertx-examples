package io.vertx.examples.mongo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoClientVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", MongoClientVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {

    JsonObject config = Vertx.currentContext().config();

    String uri = config.getString("mongo_uri");
    if (uri == null) {
      uri = "mongodb://localhost:27017";
    }
    String db = config.getString("mongo_db");
    if (db == null) {
      db = "test";
    }

    JsonObject mongoconfig = new JsonObject()
      .put("connection_string", uri)
      .put("db_name", db);

    MongoClient mongoClient = MongoClient.createShared(vertx, mongoconfig);

    JsonObject product1 = new JsonObject().put("itemId", "12345").put("name", "Cooler").put("price", "100.0");

    mongoClient.save("products", product1)
      .compose(id -> {
        System.out.println("Inserted id: " + id);
        return mongoClient.find("products", new JsonObject().put("itemId", "12345"));
      })
      .compose(res -> {
        System.out.println("Name is " + res.get(0).getString("name"));
        return mongoClient.removeDocument("products", new JsonObject().put("itemId", "12345"));
      })
      .onComplete(ar -> {
        if (ar.succeeded()) {
          System.out.println("Product removed ");
        } else {
          ar.cause().printStackTrace();
        }
      });
  }
}
