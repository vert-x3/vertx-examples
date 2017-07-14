var Router = require("vertx-web-js/router");
var metricRegistry = Java.type("com.codahale.metrics.SharedMetricRegistries").getOrCreate("exported");
Java.type("io.prometheus.client.CollectorRegistry").defaultRegistry.register(new (Java.type("io.prometheus.client.dropwizard.DropwizardExports"))(metricRegistry));

//Bind metrics handler to /metrics
var router = Router.router(vertx);
router.get("/metrics").handler(new (Java.type("io.prometheus.client.vertx.MetricsHandler"))());

//Start httpserver on localhost:8080
vertx.createHttpServer().requestHandler(router.accept).listen(8080);

//Increase counter every second
vertx.setPeriodic(1000, function (e) {
  metricRegistry.counter("testCounter").inc();
});
