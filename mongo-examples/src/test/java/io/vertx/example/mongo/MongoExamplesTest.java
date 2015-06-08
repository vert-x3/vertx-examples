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
public class MongoExamplesTest extends MongoTestBase {

  private static Vertx vertx;
  private static String temp_db_name = "test_db_" + UUID.randomUUID();
  private MongoExamples mongoExamples;
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
    this.mongoExamples = new MongoExamples(mongoService);
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

  @Test
  public void testReplace(TestContext context) {
    JsonObject object = new JsonObject().put("title", "book title2");
    Async async = context.async();
    this.mongoExamples
        .save(saveResult -> {
          this.mongoExamples.replace(result -> {
            if (result.succeeded()) {
              this.mongoService.find("books", object,
                  bookResult -> context.assertEquals(object.getString("title"), bookResult.result().get(0).getString("title")));
              async.complete();
            } else {
              context.fail();
            }
          });
        });

  }

  @Test
  public void testFind(TestContext context) {
    Async async = context.async();
    this.mongoExamples.save(saveResult -> {
      this.mongoExamples.find(result -> {
        if (result.succeeded()) {
          context.assertTrue(result.result().size() > 0);
          async.complete();
        } else {
          context.fail();
        }
      });
    });
  }

  @Test
  public void testFindOne(TestContext context) {
    Async async = context.async();
    this.mongoExamples.save(saveResult -> {
      this.mongoExamples.findOne(result -> {
        if (result.succeeded()) {
          context.assertNull(result.result().getString("edition"));
          async.complete();
        } else {
          context.fail();
        }
      });
    });
  }

  @Test
  public void testRemove(TestContext context) {
    Async async = context.async();
    this.mongoExamples.remove(removeResult -> {
      this.mongoExamples.find(result -> {
        if (result.succeeded()) {
          context.assertEquals(0, result.result().size());
          async.complete();
        } else {
          context.fail();
        }
      });
    });
  }

  @Test
  public void testRemoveOne(TestContext context) {
    Async async = context.async();
    this.mongoExamples.save(saveResult1 -> {
      this.mongoExamples.save(saveResult2 -> {
        this.mongoExamples.removeOne(removeResult -> {
          this.mongoExamples.find(result -> {
            if (result.succeeded()) {
              context.assertTrue(result.result().size() > 0);
              async.complete();
            } else {
              context.fail();
            }
          });
        });
      });
    });
  }

  @Test
  public void testCount(TestContext context) {
    Async async = context.async();
    this.mongoExamples.save(saveResult -> {
      this.mongoExamples.count(result -> {
        if (result.succeeded()) {
          context.assertTrue(result.result() > 0);
          async.complete();
        } else {
          context.fail();
        }
      });
    });
  }

  @Test
  public void testCollections(TestContext context) {
    Async async = context.async();
    this.mongoExamples.createCollection(saveResult -> {
      this.mongoExamples.getCollections(result -> {
        if (result.succeeded()) {
          context.assertTrue(result.result().size() > 0);
          context.assertTrue(result.result().contains("mynewcollection"));
          async.complete();
        } else {
          context.fail();
        }
      });
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
