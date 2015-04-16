package io.vertx.example.mongo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;
import io.vertx.ext.mongo.MongoServiceVerticle;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Erwin de Gier
 *
 */
@RunWith(VertxUnitRunner.class)
public class MongoDatabaseCommandsTest extends MongoTestBase {

  private static Vertx vertx;
  private static String temp_db_name = "test_db_" + UUID.randomUUID();
  private MongoDatabaseCommands mongoDatabaseCommands;
  private MongoService mongoService;

  @BeforeClass
  public static void setUpStatic(TestContext context) {
    Async async = context.async();
    vertx = Vertx.vertx();
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put("address", "mongo-address").put("db_name", temp_db_name));
    vertx.deployVerticle(new MongoServiceVerticle(), options, result -> {
      if (result.succeeded()) {
        async.complete();
      } else {
        context.fail("Could not deploy verticle");
      }
    });
  }

  @Before
  public void setUp(TestContext context) {
    this.mongoService = MongoService.createEventBusProxy(vertx, "mongo-address");
    this.mongoDatabaseCommands = new MongoDatabaseCommands(mongoService);
  }

  @Test
  public void testPing(TestContext context) {
    Async async = context.async();
    this.mongoDatabaseCommands.ping(result -> {
      if (result.succeeded()) {
        context.assertTrue(result.result().containsKey("ok"));
        async.complete();
      } else {
        context.fail();
      }
    });
  }

  @AfterClass
  public static void tearDown(TestContext context) {
    Async async = context.async();
    MongoService.createEventBusProxy(vertx, "mongo-address").runCommand(new JsonObject().put("dropDatabase", 1), res -> {
      vertx.close(ar -> {
        async.complete();
      });
    });
  }

}
