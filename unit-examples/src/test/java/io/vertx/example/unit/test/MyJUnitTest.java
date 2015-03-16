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
    Async async = context.async();
    server =
      vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).listen(8080, res -> {
        if (res.succeeded()) {
          async.complete();
        } else {
          context.fail();
        }
      });
  }

  @After
  public void after(TestContext context) {
    Async async = context.async();
    vertx.close(async);
  }

  @Test
  public void test1(TestContext test) {
    // Send a request and get a response
    HttpClient client = vertx.createHttpClient();
    Async async = test.async();
    client.getNow(8080, "localhost", "/", resp -> {
      resp.bodyHandler(body -> test.assertEquals("foo", body.toString()));
      client.close();
      async.complete();
    });
  }

  @Test
  public void test2(TestContext test) {
    // Deploy and undeploy a verticle
    Async async = test.async();
    vertx.deployVerticle(SomeVerticle.class.getName(), res -> {
      if (res.succeeded()) {
        String deploymentID = res.result();
        vertx.undeploy(deploymentID, res2 -> {
          if (res2.succeeded()) {
            async.complete();
          } else {
            test.fail();
          }
        });
      } else {
        test.fail();
      }
    });
  }
}
