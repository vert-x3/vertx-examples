import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine

// To simplify the development of the web components we use a Router to route all HTTP requests
// to organize our code in a reusable way.
def router = Router.router(vertx)

// In order to use a template we first need to create an engine
def engine = PebbleTemplateEngine.create(vertx)

// Entry point to the application, this will render a custom template.
router.get().handler({ ctx ->
  // we define a hardcoded title for our application
  ctx.put("name", "Vert.x Web")

  // and now delegate to the engine to render it.
  engine.render(ctx, "templates/index.peb", { res ->
    if (res.succeeded()) {
      ctx.response().end(res.result())
    } else {
      ctx.fail(res.cause())
    }
  })
})

// start a HTTP web server on port 8080
vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
