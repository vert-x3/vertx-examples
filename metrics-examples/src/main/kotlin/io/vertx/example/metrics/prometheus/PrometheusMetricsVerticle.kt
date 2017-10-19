package io.vertx.example.metrics.prometheus

import io.vertx.ext.web.Router

class PrometheusMetricsVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var metricRegistry = com.codahale.metrics.SharedMetricRegistries.getOrCreate("exported")
    io.prometheus.client.CollectorRegistry.defaultRegistry.register(io.prometheus.client.dropwizard.DropwizardExports(metricRegistry))

    //Bind metrics handler to /metrics
    var router = Router.router(vertx)
    router.get("/metrics").handler(io.prometheus.client.vertx.MetricsHandler())

    //Start httpserver on localhost:8080
    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)

    //Increase counter every second
    vertx.setPeriodic(1000L, { e ->
      metricRegistry.counter("testCounter").inc()
    })
  }
}
