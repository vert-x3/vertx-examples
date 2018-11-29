package io.vertx.example.web.blockinghandler

import io.vertx.ext.web.Router

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    router.route().blockingHandler({ routingContext ->
      // Blocking handlers are allowed to block the calling thread
      // So let's simulate a blocking action or long running operation
      try {
        java.lang.Thread.sleep(5000)
      } catch(e: Exception) {
      }


      // Now call the next handler
      routingContext.next()
    }, false)

    router.route().handler({ routingContext ->
      routingContext.response().putHeader("content-type", "text/html").end("Hello World!")
    })

    vertx.createHttpServer().requestHandler(router).listen(8080)
  }
}
