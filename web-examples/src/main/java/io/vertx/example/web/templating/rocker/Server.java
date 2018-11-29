package io.vertx.example.web.templating.rocker;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.rocker.RockerTemplateEngine;

/**
 * @author <a href="mailto:danielrauf@gmail.com>Daniel Rauf</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    final Router router = Router.router(vertx);

    // Populate context with data
    router.route().handler(ctx -> {
      ctx.put("title", "Vert.x Web Example Using Rocker");
      ctx.put("name", "Rocker");
      ctx.next();
    });

    // Render a custom template.
    // Note: you need a compile-time generator for Rocker to work properly
    // See the pom.xml for an example
    router.route().handler(TemplateHandler.create(RockerTemplateEngine.create()));

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}
