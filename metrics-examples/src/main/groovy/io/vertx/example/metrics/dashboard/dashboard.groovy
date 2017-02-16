import io.vertx.ext.dropwizard.MetricsService
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.handler.StaticHandler

def service = MetricsService.create(vertx)

def router = Router.router(vertx)

// Allow outbound traffic to the news-feed address

def options = [
  outboundPermitteds:[
    [
      address:"metrics"
    ]
  ]
]

router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options))

// Serve the static resources
router.route().handler(StaticHandler.create())

def httpServer = vertx.createHttpServer()
httpServer.requestHandler(router.&accept).listen(8080)

// Send a metrics events every second
vertx.setPeriodic(1000, { t ->
  def metrics = service.getMetricsSnapshot(vertx.eventBus())
  vertx.eventBus().publish("metrics", metrics)
})

// Send some messages
def random = new java.util.Random()
vertx.eventBus().consumer("whatever", { msg ->
  vertx.setTimer(10 + random.nextInt(50), { id ->
    vertx.eventBus().send("whatever", "hello")
  })
})
vertx.eventBus().send("whatever", "hello")
