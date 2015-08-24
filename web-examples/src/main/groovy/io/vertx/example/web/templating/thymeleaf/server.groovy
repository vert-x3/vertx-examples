import io.vertx.groovy.ext.web.templ.ThymeleafTemplateEngine
import io.vertx.groovy.ext.web.Router

// In order to use a JADE template we first need to create an engine
def engine = ThymeleafTemplateEngine.create()

// To simplify the development of the web components we use a Router to route all HTTP requests
// to organize our code in a reusable way.
def router = Router.router(vertx)

// Entry point to the application, this will render a custom JADE template.
router.get().handler({ ctx ->
  // we define a hardcoded title for our application
  ctx.put("welcome", "Hi there!")

  // and now delegate to the engine to render it.
  engine.render(ctx, "templates/index.html", { res ->
    if (res.succeeded()) {
      ctx.response().end(res.result())
    } else {
      ctx.fail(res.cause())
    }
  })
})

// start a HTTP web server on port 8080
vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
