package io.vertx.example.unit.test;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.example.unit.HelloVerticle;
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
    vertx
      .deployVerticle(HelloVerticle.class.getName(), options)
      .onComplete(context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx
      .close()
      .onComplete(context.asyncAssertSuccess());
  }

  @Test
  public void canGetHello(TestContext context) {
    HttpClient client = vertx.createHttpClient();
    client.request(HttpMethod.GET, port, "localhost", "/")
      .compose(req -> req
        .send()
        .compose(HttpClientResponse::body))
      .onComplete(context.asyncAssertSuccess(body -> {
        context.assertEquals("Hello!", body.toString());
        client.close();
    }));
  }
}
