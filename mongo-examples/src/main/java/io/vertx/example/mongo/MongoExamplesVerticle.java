package io.vertx.example.mongo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;
import io.vertx.ext.mongo.MongoServiceVerticle;

public class MongoExamplesVerticle extends AbstractVerticle {
  private static final String MONGO_ADDRESS = "mongo-address";

  @Override
  public void start() throws Exception {
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put("address", MONGO_ADDRESS));
    vertx.deployVerticle(new MongoServiceVerticle(), options, res -> {

      if (!res.succeeded()) {
        res.cause().printStackTrace();
      }

    });
    // pass an eventbus proxy instance of the MongoService to the examples class
    new MongoExamples(MongoService.createEventBusProxy(vertx, MONGO_ADDRESS));
  }

}
