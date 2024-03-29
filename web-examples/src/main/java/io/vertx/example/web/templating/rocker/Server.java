package io.vertx.example.web.templating.rocker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.rocker.RockerTemplateEngine;

/**
 * @author <a href="mailto:danielrauf@gmail.com>Daniel Rauf</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() throws Exception {

    final Router router = Router.router(vertx);

    // Populate context with data
    router.route().handler(ctx -> {
      ctx.put("title", "Vert.x Web Example Using Rocker");
      ctx.put("name", "Rocker");
      ctx.put("path", ctx.request().path());
      ctx.next();
    });

    // Render a custom template.
    // Note: you need a compile-time generator for Rocker to work properly
    // See the pom.xml for an example
    router.route().handler(TemplateHandler.create(RockerTemplateEngine.create()));

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}
