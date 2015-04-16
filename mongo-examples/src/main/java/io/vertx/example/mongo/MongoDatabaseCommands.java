package io.vertx.example.mongo;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;

/**
 * @author Erwin de Gier
 *
 */
public class MongoDatabaseCommands {
  private MongoService mongoService;

  /**
   * Creates a new MongoDatabaseCommands instance.
   * 
   * @param mongoService
   *          A reference to a MongoService (proxy) instance which is used to
   *          perform the interactions with MongoDB.
   */
  public MongoDatabaseCommands(MongoService mongoService) {
    this.mongoService = mongoService;
  }

  public void ping(Handler<AsyncResult<JsonObject>> handler) {
    this.mongoService.runCommand(new JsonObject().put("ping", 1), result -> {
      if (result.succeeded()) {
        System.out.println("Result: " + result.result().encodePrettily());
      } else {
        result.cause().printStackTrace();
      }
      handler.handle(result);
    });
  }
}
