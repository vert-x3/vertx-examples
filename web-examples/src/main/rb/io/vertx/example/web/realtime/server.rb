require 'vertx-web/router'
require 'vertx-web/sock_js_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# Allow outbound traffic to the news-feed address

options = {
  'outboundPermitteds' => [
    {
      'address' => "news-feed"
    }
  ]
}

router.route("/eventbus/*").handler(&VertxWeb::SockJSHandler.create($vertx).bridge(options).method(:handle))

# Serve the static resources
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

# Publish a message to the address "news-feed" every second
$vertx.set_periodic(1000) { |t|
  $vertx.event_bus().publish("news-feed", "news from the server!");
}
