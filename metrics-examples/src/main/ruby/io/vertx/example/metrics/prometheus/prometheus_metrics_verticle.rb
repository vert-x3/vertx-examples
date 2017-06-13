require 'vertx-web/router'
metricRegistry = Java::ComCodahaleMetrics::SharedMetricRegistries.get_or_create("exported")
Java::IoPrometheusClient::CollectorRegistry::defaultRegistry.register(Java::IoPrometheusClientDropwizard::DropwizardExports.new(metricRegistry))

#Bind metrics handler to /metrics
router = VertxWeb::Router.router($vertx)
router.get("/metrics").handler(&Java::IoPrometheusClientVertx::MetricsHandler.new())

#Start httpserver on localhost:8080
$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

#Increase counter every second
$vertx.set_periodic(1000) { |e|
  metricRegistry.counter("testCounter").inc()
}
