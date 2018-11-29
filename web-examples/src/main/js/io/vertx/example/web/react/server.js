var Router = require("vertx-web-js/router");
var SockJSHandler = require("vertx-web-js/sock_js_handler");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Allow events for the designated addresses in/out of the event bus bridge
var opts = {
  "inboundPermitteds" : [
    {
      "address" : "chat.message"
    }
  ],
  "outboundPermitteds" : [
    {
      "address" : "chat.message"
    }
  ]
};

// Create the event bus bridge and add it to the router.
var ebHandler = SockJSHandler.create(vertx).bridge(opts);
router.route("/eventbus/*").handler(ebHandler.handle);

// Create a router endpoint for the static content.
router.route().handler(StaticHandler.create().handle);

// Start the web server and tell it to use the router to handle requests.
vertx.createHttpServer().requestHandler(router.handle).listen(8080);
