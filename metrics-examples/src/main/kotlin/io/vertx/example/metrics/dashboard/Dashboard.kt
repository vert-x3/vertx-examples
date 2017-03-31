package io.vertx.example.metrics.dashboard

import io.vertx.ext.dropwizard.MetricsService
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.BridgeOptions
import io.vertx.ext.web.handler.sockjs.PermittedOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.kotlin.ext.web.handler.sockjs.*

class Dashboard : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var service = MetricsService.create(vertx)

    var router = Router.router(vertx)

    // Allow outbound traffic to the news-feed address

    var options = BridgeOptions(
      outboundPermitteds = listOf(PermittedOptions(
        address = "metrics")))

    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options))

    // Serve the static resources
    router.route().handler(StaticHandler.create())

    var httpServer = vertx.createHttpServer()
    httpServer.requestHandler({ router.accept(it) }).listen(8080)

    // Send a metrics events every second
    vertx.setPeriodic(1000, { t ->
      var metrics = service.getMetricsSnapshot(vertx.eventBus())
      vertx.eventBus().publish("metrics", metrics)
    })

    // Send some messages
    var random = java.util.Random()
    vertx.eventBus().consumer<Any>("whatever", { msg ->
      vertx.setTimer(10 + random.nextInt(50), { id ->
        vertx.eventBus().send("whatever", "hello")
      })
    })
    vertx.eventBus().send("whatever", "hello")
  }
}
