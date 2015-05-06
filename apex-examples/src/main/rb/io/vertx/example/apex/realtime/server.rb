require 'vertx-apex/router'
require 'vertx-apex/sock_js_handler'
require 'vertx-apex/static_handler'

router = VertxApex::Router.router($vertx)

# Allow outbound traffic to the news-feed address

options = {
  'outboundPermitteds' => [
    {
      'address' => "news-feed"
    }
  ]
}

router.route("/eventbus/*").handler(&VertxApex::SockJSHandler.create($vertx).bridge(options).method(:handle))

# Serve the static resources
router.route().handler(&VertxApex::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

# Publish a message to the address "news-feed" every second
$vertx.set_periodic(1000) { |t|
  $vertx.event_bus().publish("news-feed", "news from the server!")}
