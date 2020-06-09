package io.vertx.example.unit.test;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;

/*
 * Example of asynchronous unit test written in raw vertx-unit style
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class VertxUnitTest {


  public static void main(String[] args) {
    new VertxUnitTest().run();
  }

  Vertx vertx;

  @CodeTranslate // Not yet detected
  public void run() {

    TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console"));
    TestSuite suite = TestSuite.create("io.vertx.example.unit.test.VertxUnitTest");

    suite.before(context -> {
      vertx = Vertx.vertx();
      vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).listen(8080, context.asyncAssertSuccess());
    });

    suite.after(context -> {
      vertx.close(context.asyncAssertSuccess());
    });

    // Specifying the test names seems ugly...
    suite.test("some_test1", context -> {
      // Send a request and get a response
      HttpClient client = vertx.createHttpClient();
      Async async = context.async();
      client.get(8080, "localhost", "/")
        .flatMap(HttpClientResponse::body)
        .onSuccess(body -> {
          context.assertEquals("foo", body.toString("UTF-8"));
          client.close();
          async.complete();
        }).onFailure(context::fail);
    });
    suite.test("some_test2", context -> {
      // Deploy and undeploy a verticle
      vertx.deployVerticle("io.vertx.example.unit.SomeVerticle", context.asyncAssertSuccess(deploymentID -> {
        vertx.undeploy(deploymentID, context.asyncAssertSuccess());
      }));
    });

    suite.run(options);
  }
}
