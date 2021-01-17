package io.vertx.examples.resteasy;

import io.swagger.v3.core.util.Json;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.resteasy.asyncresponse.Main;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class RestEasyTest {

  private static WebClient webClient;
  private static Vertx vertx;

  @BeforeClass
  public static void setUp() {


    vertx = Vertx.vertx();
    vertx.deployVerticle(new Main());
    webClient = WebClient.create(vertx, new WebClientOptions()
      .setDefaultPort(8080));

  }

  @AfterClass
  public static void tearDown() throws Exception {
    vertx.close();
  }

  @Test
  public void base(TestContext context) {
    Async async = context.async();
    Promise<HttpResponse<JsonObject>> promise = Promise.promise();
    webClient.get("/products")
      .send(ar -> {
        if (ar.succeeded()) {
          JsonArray jsonArray = ar.result().bodyAsJsonArray();
          System.out.println(Json.pretty(jsonArray));
          context.assertEquals(3, jsonArray.size());
          async.complete();
        } else {
          context.fail(ar.cause());
          async.complete();
        }
      });
    async.awaitSuccess(5000);
  }

  @Test
  public void openApi(TestContext context) {
    Async async = context.async();
    Promise<HttpResponse<JsonObject>> promise = Promise.promise();
    webClient.get("/openapi.json")
      .send(ar -> {
        if (ar.succeeded()) {
          JsonObject jsonArray = ar.result().bodyAsJsonObject();
          context.assertEquals(2, jsonArray.size());
          context.assertEquals("3.0.1",jsonArray.getString("openapi"));
          async.complete();
        } else {
          context.fail(ar.cause());
          async.complete();
        }
      });
    async.awaitSuccess(5000);
  }


}
