package io.vertx.example.web.staticsite

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    // Serve the static pages
    router.route().handler(StaticHandler.create())

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)

    println("Server is started")

  }
}
