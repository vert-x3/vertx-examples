import io.vertx.groovy.ext.web.Router
import io.vertx.ext.web.handler.sockjs.BridgeEventType
import io.vertx.groovy.ext.web.handler.sockjs.SockJSHandler
import io.vertx.groovy.ext.web.handler.StaticHandler

def router = Router.router(vertx)

// Allow outbound traffic to the news-feed address

def options = [
  outboundPermitteds:[
    [
      address:"news-feed"
    ]
  ]
]

router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, { event ->

  // You can also optionally provide a handler like this which will be passed any events that occur on the bridge
  // You can use this for monitoring or logging, or to change the raw messages in-flight.
  // It can also be used for fine grained access control.

  if (event.type() == BridgeEventType.SOCKET_CREATED) {
    println("A socket was created")
  }

  // This signals that it's ok to process the event
  event.complete(true)

}))

// Serve the static resources
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)

// Publish a message to the address "news-feed" every second
vertx.setPeriodic(1000, { t ->
  vertx.eventBus().publish("news-feed", "news from the server!")
})
