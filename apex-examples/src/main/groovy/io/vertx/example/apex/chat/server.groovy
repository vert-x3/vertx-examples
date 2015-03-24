import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.sockjs.SockJSHandler
import io.vertx.groovy.ext.apex.handler.StaticHandler

def router = Router.router(vertx)

// Allow events for the designated addresses in/out of the event bus bridge
def opts = [
  inboundPermitteds:[
    [
      address:"chat.to.server"
    ]
  ],
  outboundPermitteds:[
    [
      address:"chat.to.client"
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

def eb = vertx.eventBus()

// Register to listen for messages coming IN to the server
eb.consumer("chat.to.server").handler({ message ->
  // Create a timestamp string
  def timestamp = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.MEDIUM).format(java.util.Date.from(java.time.Instant.now()))
  // Send the message back out to all clients with the timestamp prepended.
  eb.publish("chat.to.client", "${timestamp}: ${message.body()}")
})

