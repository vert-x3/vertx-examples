import io.vertx.ext.web.Router
def metricRegistry = com.codahale.metrics.SharedMetricRegistries.getOrCreate("exported")
io.prometheus.client.CollectorRegistry.defaultRegistry.register(new io.prometheus.client.dropwizard.DropwizardExports(metricRegistry))

//Bind metrics handler to /metrics
def router = Router.router(vertx)
router.get("/metrics").handler(new io.prometheus.client.vertx.MetricsHandler())

//Start httpserver on localhost:8080
vertx.createHttpServer().requestHandler(router.&accept).listen(8080)

//Increase counter every second
vertx.setPeriodic(1000L, { e ->
  metricRegistry.counter("testCounter").inc()
})
