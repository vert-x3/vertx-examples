package io.vertx.example.web.cors

import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.OPTIONS).allowedHeader("X-PINGARUNER").allowedHeader("Content-Type"))

    router.get("/access-control-with-get").handler({ ctx ->

      ctx.response().setChunked(true)

      var headers = ctx.request().headers()
      for (key in headers.names()) {
        ctx.response().write(key)
        ctx.response().write(headers.get(key))
        ctx.response().write("\n")
      }

      ctx.response().end()
    })

    router.post("/access-control-with-post-preflight").handler({ ctx ->
      ctx.response().setChunked(true)

      var headers = ctx.request().headers()
      for (key in headers.names()) {
        ctx.response().write(key)
        ctx.response().write(headers.get(key))
        ctx.response().write("\n")
      }

      ctx.response().end()
    })

    // Serve the static resources
    router.route().handler(StaticHandler.create())

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
