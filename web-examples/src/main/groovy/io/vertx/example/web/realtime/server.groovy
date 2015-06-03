import io.vertx.groovy.ext.web.Router
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

router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options))

// Serve the static resources
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)

// Publish a message to the address "news-feed" every second
vertx.setPeriodic(1000, { t ->
  vertx.eventBus().publish("news-feed", "news from the server!");
})
