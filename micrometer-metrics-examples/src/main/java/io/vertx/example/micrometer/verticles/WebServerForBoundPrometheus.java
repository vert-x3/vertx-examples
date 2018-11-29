package io.vertx.example.micrometer.verticles;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.backends.BackendRegistries;

import java.util.function.Consumer;

/**
 * @author Joel Takvorian, jtakvori@redhat.com
 */
public class WebServerForBoundPrometheus extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    PrometheusMeterRegistry registry = (PrometheusMeterRegistry) BackendRegistries.getDefaultNow();
    // Setup a route for metrics
    router.route("/metrics").handler(ctx -> {
      String response = registry.scrape();
      ctx.response().end(response);
    });
    router.get("/").handler(ctx -> {
      Greetings.get(vertx, greetingResult -> ctx.response().end(greetingResult.result()));
    });
    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}
