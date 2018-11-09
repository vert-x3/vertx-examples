package io.vertx.example.web.templating.pebble

import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine
import io.vertx.kotlin.core.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    var router = Router.router(vertx)

    // In order to use a template we first need to create an engine
    var engine = PebbleTemplateEngine.create(vertx)

    // Entry point to the application, this will render a custom template.
    router.get().handler({ ctx ->
      // we define a hardcoded title for our application
      var data = json {
        obj(
          "name" to "Vert.x Web",
          "path" to ctx.request().path()
        )
      }

      // and now delegate to the engine to render it.
      engine.render(data, "templates/index.peb", { res ->
        if (res.succeeded()) {
          ctx.response().end(res.result())
        } else {
          ctx.fail(res.cause())
        }
      })
    })

    // start a HTTP web server on port 8080
    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
