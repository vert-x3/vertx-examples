package io.vertx.example.mongo;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;
import io.vertx.ext.mongo.MongoServiceVerticle;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MongoExamplesIntegrationTest {

  private Vertx vertx;
  private MongoExamples mongoExamples;
  private MongoService mongoService;

  @Before
  public void setUp(TestContext context) {
    Async async = context.async();
    vertx = Vertx.vertx();
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject().put("address", "mongo-address"));
    vertx.deployVerticle(new MongoServiceVerticle(), options, result -> {
      if (result.succeeded()) {
        async.complete();
      } else {
        context.fail("Could not deploy verticle");
      }
    });
    this.mongoService = MongoService.createEventBusProxy(vertx, "mongo-address");
    this.mongoExamples = new MongoExamples(this.mongoService);
  }

  @Test
  public void testSave(TestContext context) {
    Async async = context.async();
    JsonObject object = new JsonObject().put("title", "book title");
    this.mongoExamples.save(result -> {
      if (result.succeeded()) {
        this.mongoService.find("books", object, bookResult -> context.assertEquals(object.getString("title"), bookResult.result().get(0).getString("title")));
        async.complete();
      } else {
        context.fail();
      }
    });
  }

  @Test
  public void testSaveWithId(TestContext context) {
    Async async = context.async();
    JsonObject object = new JsonObject().put("title", "book title");
    this.mongoExamples.saveWithId(result -> {
      if (result.succeeded()) {
        this.mongoService.find("books", object, bookResult -> {
          context.assertEquals(object, bookResult.result().get(0));
          context.assertEquals("123244", bookResult.result().get(0).getString("_id"));
        });
        async.complete();
      } else {
        context.fail();
      }
    });
  }

  @After
  public void tearDown(TestContext context) {
    Async async = context.async();
    vertx.close(ar -> {
      async.complete();
    });
  }
}
