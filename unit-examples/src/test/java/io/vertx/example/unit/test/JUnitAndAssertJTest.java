package io.vertx.example.unit.test;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * Example of an asynchronous unit test written in JUnit style using vertx-unit and AssertJ
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@RunWith(VertxUnitRunner.class)
public class JUnitAndAssertJTest {

  Vertx vertx;
  HttpServer server;

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();

    // Register the context exception handler
    vertx.exceptionHandler(context.exceptionHandler());

    server =
        vertx.createHttpServer().requestHandler(req -> req.response().end("foo"));
    server.
      listen(8080)
      .onComplete(context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx.close().onComplete(context.asyncAssertSuccess());
  }

  @Test
  public void testAsyncOperation(TestContext context) {
    Async async = context.async();

    getSomeItems(list -> {
      assertThat(list).contains("a", "b", "c");
      async.complete();
    });

  }

  @Test
  public void testHttpCall(TestContext context) {
    // Send a request and get a response
    HttpClient client = vertx.createHttpClient();
    client.request(HttpMethod.GET, 8080, "localhost", "/")
      .compose(req -> req
        .send()
        .compose(HttpClientResponse::body))

      // No need for an async creation as asyncAssertSuccess does it for you
      .onComplete(context.asyncAssertSuccess(body -> {
        assertThat(body.toString()).isEqualTo("foo");
        client.close();
      }));
  }

  private void getSomeItems(Handler<List<String>> handler) {
    // Just there to mimic some IO, and the answer arrive later.
    vertx.setTimer(10, l -> handler.handle(Arrays.asList("a", "b", "c")));
  }
}
