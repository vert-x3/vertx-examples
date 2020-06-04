package io.vertx.example.web.chat

import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.kotlin.ext.bridge.*
import io.vertx.kotlin.ext.web.handler.sockjs.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    // Allow events for the designated addresses in/out of the event bus bridge
    var opts = SockJSBridgeOptions(
      inboundPermitteds = listOf(PermittedOptions(
        address = "chat.to.server")),
      outboundPermitteds = listOf(PermittedOptions(
        address = "chat.to.client")))

    // Create the event bus bridge and add it to the router.
    var ebHandler = SockJSHandler.create(vertx)
    router.mountSubRouter("/eventbus", ebHandler.bridge(opts))

    // Create a router endpoint for the static content.
    router.route().handler(StaticHandler.create())

    // Start the web server and tell it to use the router to handle requests.
    vertx.createHttpServer().requestHandler(router).listen(8080)

    var eb = vertx.eventBus()

    // Register to listen for messages coming IN to the server
    eb.consumer<Any>("chat.to.server").handler({ message ->
      // Create a timestamp string
      var timestamp = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.MEDIUM).format(java.util.Date.from(java.time.Instant.now()))
      // Send the message back out to all clients with the timestamp prepended.
      eb.publish("chat.to.client", "${timestamp}: ${message.body()}")
    })

  }
}
