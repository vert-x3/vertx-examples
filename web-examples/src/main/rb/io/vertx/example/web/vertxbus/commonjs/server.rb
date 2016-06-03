require 'vertx-web/router'
require 'vertx-web/sock_js_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# Allow events for the designated addresses in/out of the event bus bridge
opts = {
  'outboundPermitteds' => [
    {
      'address' => "feed"
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

eb = $vertx.event_bus()

$vertx.set_periodic(1000) { |t|
  # Create a timestamp string
  timestamp = Java::JavaText::DateFormat.get_date_time_instance(Java::JavaText::DateFormat::SHORT, Java::JavaText::DateFormat::MEDIUM).format(Java::JavaUtil::Date.from(Java::JavaTime::Instant.now()))

  eb.send("feed", {
    'now' => timestamp
  })
}
