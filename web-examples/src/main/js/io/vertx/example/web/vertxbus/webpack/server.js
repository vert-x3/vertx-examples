var Router = require("vertx-web-js/router");
var SockJSHandler = require("vertx-web-js/sock_js_handler");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Allow events for the designated addresses in/out of the event bus bridge
var opts = {
  "inboundPermitteds" : [
    {
      "address" : "com.example:cmd:poke-server"
    }
  ],
  "outboundPermitteds" : [
    {
      "address" : "com.example:stat:server-info"
    }
  ]
};

// Create the event bus bridge and add it to the router.
var ebHandler = SockJSHandler.create(vertx).bridge(opts);
router.route("/eventbus/*").handler(ebHandler.handle);

// Create a router endpoint for the static content.
router.route().handler(StaticHandler.create().handle);

// Start the web server and tell it to use the router to handle requests.
vertx.createHttpServer().requestHandler(router.accept).listen(8080);

var eb = vertx.eventBus();

vertx.setPeriodic(1000, function (t) {
  // Create a timestamp string
  var timestamp = Java.type("java.text.DateFormat").getDateTimeInstance(Java.type("java.text.DateFormat").SHORT, Java.type("java.text.DateFormat").MEDIUM).format(Java.type("java.util.Date").from(Java.type("java.time.Instant").now()));
  eb.send("com.example:stat:server-info", {
    "systemTime" : timestamp
  });
});
