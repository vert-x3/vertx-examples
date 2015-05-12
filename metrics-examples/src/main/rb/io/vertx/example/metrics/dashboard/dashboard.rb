require 'vertx-dropwizard/metrics_service'
require 'vertx-apex/router'
require 'vertx-apex/sock_js_handler'
require 'vertx-apex/static_handler'

service = VertxDropwizard::MetricsService.create($vertx)

router = VertxApex::Router.router($vertx)

# Allow outbound traffic to the news-feed address

options = {
  'outboundPermitteds' => [
    {
      'address' => "metrics"
    }
  ]
}

router.route("/eventbus/*").handler(&VertxApex::SockJSHandler.create($vertx).bridge(options).method(:handle))

# Serve the static resources
router.route().handler(&VertxApex::StaticHandler.create().method(:handle))

httpServer = $vertx.create_http_server()
httpServer.request_handler(&router.method(:accept)).listen(8080)

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
