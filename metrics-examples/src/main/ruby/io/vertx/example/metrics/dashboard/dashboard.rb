require 'vertx-dropwizard/metrics_service'
require 'vertx-web/router'
require 'vertx-web/sock_js_handler'
require 'vertx-web/static_handler'

service = VertxDropwizard::MetricsService.create($vertx)

router = VertxWeb::Router.router($vertx)

# Allow outbound traffic to the news-feed address

options = {
  'outboundPermitteds' => [
    {
      'address' => "metrics"
    }
  ]
}

router.route("/eventbus/*").handler(&VertxWeb::SockJSHandler.create($vertx).bridge(options).method(:handle))

# Serve the static resources
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

httpServer = $vertx.create_http_server()
httpServer.request_handler(&router.method(:handle)).listen(8080)

# Send a metrics events every second
$vertx.set_periodic(1000) { |t|
  metrics = service.get_metrics_snapshot($vertx.event_bus())
  $vertx.event_bus().publish("metrics", metrics)
}

# Send some messages
random = Java::JavaUtil::Random.new()
$vertx.event_bus().consumer("whatever") { |msg|
  $vertx.set_timer(10 + random.next_int(50)) { |id|
    $vertx.event_bus().send("whatever", "hello")
  }
}
$vertx.event_bus().send("whatever", "hello")
