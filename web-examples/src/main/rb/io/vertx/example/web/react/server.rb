require 'vertx-web/router'
require 'vertx-web/sock_js_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# Allow events for the designated addresses in/out of the event bus bridge
opts = {
  'inboundPermitteds' => [
    {
      'address' => "chat.message"
    }
  ],
  'outboundPermitteds' => [
    {
      'address' => "chat.message"
    }
  ]
}

# Create the event bus bridge and add it to the router.
ebHandler = VertxWeb::SockJSHandler.create($vertx).bridge(opts)
router.route("/eventbus/*").handler(&ebHandler.method(:handle))

# Create a router endpoint for the static content.
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

# Start the web server and tell it to use the router to handle requests.
$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
