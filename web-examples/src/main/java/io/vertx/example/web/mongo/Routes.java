package io.vertx.example.web.mongo;

import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.JadeTemplateEngine;

/**
 * Handler for index page.
 *
 * @author <a href="mailto:pmlopes@gmail.com>Paulo Lopes</a>
 */
public class Routes {

  // In order to use a JADE template we first need to create an engine
  final static JadeTemplateEngine jade = JadeTemplateEngine.create();

  public static void index(RoutingContext ctx) {
    // we define a hardcoded title for our application
    ctx.put("title", "Vert.x Web");

    // and now delegate to the engine to render it.
    jade.render(ctx, "templates/index", res -> {
      if (res.succeeded()) {
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
      } else {
        ctx.fail(res.cause());
      }
    });
  }
}
