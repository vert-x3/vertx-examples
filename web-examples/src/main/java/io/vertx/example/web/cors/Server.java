package io.vertx.example.web.cors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Map;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(CorsHandler.create("*")
      .allowedMethod(HttpMethod.GET)
      .allowedMethod(HttpMethod.POST)
      .allowedMethod(HttpMethod.OPTIONS)
      .allowedHeader("X-PINGARUNER")
      .allowedHeader("Content-Type"));

    router.get("/access-control-with-get").handler(ctx -> {

      ctx.response().setChunked(true);

      MultiMap headers = ctx.request().headers();
      for (String key : headers.names()) {
        ctx.response().write(key);
        ctx.response().write(headers.get(key));
        ctx.response().write("\n");
      }

      ctx.response().end();
    });

    router.post("/access-control-with-post-preflight").handler(ctx -> {
      ctx.response().setChunked(true);

      MultiMap headers = ctx.request().headers();
      for (String key : headers.names()) {
        ctx.response().write(key);
        ctx.response().write(headers.get(key));
        ctx.response().write("\n");
      }

      ctx.response().end();
    });

    // Serve the static resources
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
