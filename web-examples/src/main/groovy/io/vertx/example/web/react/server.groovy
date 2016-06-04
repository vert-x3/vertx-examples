import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.sockjs.SockJSHandler
import io.vertx.groovy.ext.web.handler.StaticHandler

def router = Router.router(vertx)

// Allow events for the designated addresses in/out of the event bus bridge
def opts = [
  inboundPermitteds:[
    [
      address:"chat.message"
    ]
  ],
  outboundPermitteds:[
    [
      address:"chat.message"
    ]
  ]
]

// Create the event bus bridge and add it to the router.
def ebHandler = SockJSHandler.create(vertx).bridge(opts)
router.route("/eventbus/*").handler(ebHandler)

// Create a router endpoint for the static content.
router.route().handler(StaticHandler.create())

// Start the web server and tell it to use the router to handle requests.
vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
