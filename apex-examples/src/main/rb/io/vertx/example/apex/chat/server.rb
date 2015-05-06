require 'vertx-apex/router'
require 'vertx-apex/sock_js_handler'
require 'vertx-apex/static_handler'

router = VertxApex::Router.router($vertx)

# Allow events for the designated addresses in/out of the event bus bridge
opts = {
  'inboundPermitteds' => [
    {
      'address' => "chat.to.server"
    }
  ],
  'outboundPermitteds' => [
    {
      'address' => "chat.to.client"
    }
  ]
}

# Create the event bus bridge and add it to the router.
ebHandler = VertxApex::SockJSHandler.create($vertx).bridge(opts)
router.route("/eventbus/*").handler(&ebHandler.method(:handle))

# Create a router endpoint for the static content.
router.route().handler(&VertxApex::StaticHandler.create().method(:handle))

# Start the web server and tell it to use the router to handle requests.
$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

eb = $vertx.event_bus()

# Register to listen for messages coming IN to the server
eb.consumer("chat.to.server").handler() { |message|
  # Create a timestamp string
  timestamp = Java::JavaText::DateFormat.get_date_time_instance(Java::JavaText::DateFormat.SHORT, Java::JavaText::DateFormat.MEDIUM).format(Java::JavaUtil::Date.from(Java::JavaTime::Instant.now()))
  # Send the message back out to all clients with the timestamp prepended.
  eb.publish("chat.to.client", "#{timestamp}: #{message.body()}")
}

