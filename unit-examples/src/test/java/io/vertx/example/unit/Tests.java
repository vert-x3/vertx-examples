package io.vertx.example.unit;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestSuite;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Tests {

  protected HttpServer server;
  protected Vertx vertx;

  public TestSuite suite() {

    TestSuite suite = TestSuite.create("my_suite");

    suite.before(test -> {
      vertx = Vertx.vertx();
      Async async = test.async();
      server =
        vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).listen(8080, res -> {
          if (res.succeeded()) {
            async.complete();
          } else {
            test.fail();
          }
        });
    });

    // Specifying the test names seems redundant...
    suite.test("some_test1", this::test1);
    suite.test("some_test2", this::test2);

    suite.after(test -> {
      Async async = test.async();
      vertx.close(res -> {
        if (res.succeeded()) {
          async.complete();
        } else {
          test.fail();
        }
      });
    });

    return suite;

  }

  public void test1(Test test) {
    // Send a request and get a response
    HttpClient client = vertx.createHttpClient();
    Async async = test.async();
    client.getNow(8080, "localhost", "/", resp -> {
      resp.bodyHandler(body -> test.assertEquals("foo", body.toString()));
      client.close();
      async.complete();
    });
  }

  public void test2(Test test) {
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
