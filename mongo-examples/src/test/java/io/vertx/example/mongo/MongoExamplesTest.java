package io.vertx.example.mongo;

import java.util.UUID;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoService;
import io.vertx.ext.mongo.MongoServiceVerticle;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MongoExamplesTest {

  private Vertx vertx;
  private MongoExamples mongoExamples;
  private MongoService mongoService;
  private String temp_db_name = "test_db_"+UUID.randomUUID();

  @Before
  public void setUp(TestContext context) {
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
    JsonObject object = new JsonObject().put("title", "new title");
    this.mongoExamples.saveWithId(result -> {
      if (result.succeeded()) {
        this.mongoService.find("books", object, bookResult -> {
          context.assertEquals(object.getString("title"), bookResult.result().get(0).getString("title"));
        });
        async.complete();
      } else {
        context.fail();
      }
    });
  }

  @Test
  public void testInsert(TestContext context) {
    Async async = context.async();
    JsonObject object = new JsonObject().put("title", "book title");
    this.mongoExamples.insert(result -> {
      if (result.succeeded()) {
        this.mongoService.find("books", object, bookResult -> context.assertEquals(object.getString("title"), bookResult.result().get(0).getString("title")));
        async.complete();
      } else {
        context.fail();
      }
    });
  }

  @Test
  public void testInsertWithId(TestContext context) {
    Async async = context.async();
    this.mongoExamples.insertWithId(result -> {
      if (!result.succeeded()) {
        async.complete();
      } else {
        context.fail();
      }
    });
  }

  @After
  public void tearDown(TestContext context) {
    Async async = context.async();
    mongoService.runCommand(new JsonObject().put("dropDatabase", 1), res -> {
      vertx.close(ar -> {
        async.complete();
      });
    });
  }
  
}
