package io.vertx.example.unit.test;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
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
      client.request(HttpMethod.GET, 8080, "localhost", "/")
        .compose(req -> req
          .send()
          .compose(HttpClientResponse::body))
        .onComplete(context.asyncAssertSuccess(body -> {
          context.assertEquals("foo", body.toString("UTF-8"));
          client.close();
        }));
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
