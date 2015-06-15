package io.vertx.example.unit.test;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.unit.junit.VertxUnitRunnerWithParametersFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * Example showing the usage of the {@link io.vertx.ext.unit.junit.RunTestOnContext} rule that allows to run
 * a junit test on a vert.x event loop thread.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class RunOnContextTest {

  /*
   * This rule wraps the junit calls in a Vert.x context, the Vert.x instance can be created by the
   * rule or provided like in this case.
   */
  @Rule
  public final RunTestOnContext rule = new RunTestOnContext(Vertx::vertx);

  private Thread thread;

  @Before
  public void before(TestContext context) {
    context.assertTrue(Context.isOnEventLoopThread());
    thread = Thread.currentThread();
  }

  @Test
  public void theTest(TestContext context) {
    context.assertTrue(Context.isOnEventLoopThread());
    context.assertEquals(thread, Thread.currentThread());
  }

  @After
  public void after(TestContext context) {
    context.assertTrue(Context.isOnEventLoopThread());
    context.assertEquals(thread, Thread.currentThread());
  }
}
