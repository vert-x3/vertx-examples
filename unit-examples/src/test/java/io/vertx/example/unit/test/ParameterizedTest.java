package io.vertx.example.unit.test;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunnerWithParametersFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * Example showing a JUnit parameterized test with Vert.x . The xample is quite simple : a simple http client/server
 * request using a parameterized port.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(Parameterized.class)
@Parameterized.UseParametersRunnerFactory(VertxUnitRunnerWithParametersFactory.class)
public class ParameterizedTest {

  /**
   * @return the test ports
   */
  @Parameterized.Parameters
  public static Iterable<Integer> ports() {
    return Arrays.asList(8080, 8081);
  }

  private final int port;
  private Vertx vertx;

  public ParameterizedTest(int port) {
    this.port = port;
  }

  @Before
  public void before() {
    vertx = Vertx.vertx();
  }

  @Test
  public void test(TestContext context) {
    HttpServer server = vertx.createHttpServer().requestHandler(req -> {
      context.assertEquals(port, req.localAddress().port());
      req.response().end();
    });
    server.listen(port, "localhost", context.asyncAssertSuccess(s -> {
      HttpClient client = vertx.createHttpClient();
      Async async = context.async();
      client.get(port, "localhost", "/")
        .onSuccess(resp -> {
          context.assertEquals(200, resp.statusCode());
          async.complete();
        })
        .onFailure(context::fail);
    }));
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }
}
