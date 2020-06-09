package io.vertx.example.unit.test;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.example.unit.HelloVerticle;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

/*
 * Example of an asynchronous JUnit test for a Verticle.
 */
@RunWith(VertxUnitRunner.class)
public class JUnitVerticleTest {

  Vertx vertx;
  int port;

  @Before
  public void before(TestContext context) throws IOException {
    ServerSocket socket = new ServerSocket(0);
    port = socket.getLocalPort();
    socket.close();

    DeploymentOptions options = new DeploymentOptions()
        .setConfig(new JsonObject().put("http.port", port));

    vertx = Vertx.vertx();
    vertx.deployVerticle(HelloVerticle.class.getName(), options, context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void canGetHello(TestContext context) {
    Async async = context.async();
    HttpClient client = vertx.createHttpClient();
    client.get(port, "localhost", "/")
      .flatMap(HttpClientResponse::body)
      .onSuccess(body -> {
        context.assertEquals("Hello!", body.toString());
        client.close();
        async.complete();
      }).onFailure(context::fail);
  }
}
