package io.vertx.example.unit.test;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.example.unit.SomeVerticle;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * Example of an asynchronous unit test written in JUnit style using vertx-unit
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@RunWith(VertxUnitRunner.class)
public class MyJUnitTest {

  Vertx vertx;
  HttpServer server;

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();
    server =
      vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).
          listen(8080, context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void test1(TestContext context) {
    // Send a request and get a response
    HttpClient client = vertx.createHttpClient();
    Async async = context.async();
    client.getNow(8080, "localhost", "/", resp -> {
      resp.bodyHandler(body -> {
        context.assertEquals("foo", body.toString());
        client.close();
        async.complete();
      });
    });
  }

  @Test
  public void test2(TestContext context) {
    // Deploy and undeploy a verticle
    vertx.deployVerticle(SomeVerticle.class.getName(), context.asyncAssertSuccess(deploymentID -> {
      vertx.undeploy(deploymentID, context.asyncAssertSuccess());
    }));
  }
}
