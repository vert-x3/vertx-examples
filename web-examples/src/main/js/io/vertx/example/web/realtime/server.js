var Router = require("vertx-web-js/router");
var BridgeEvent = require("vertx-web-js/bridge_event");
var SockJSHandler = require("vertx-web-js/sock_js_handler");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Allow outbound traffic to the news-feed address

var options = {
  "outboundPermitteds" : [
    {
      "address" : "news-feed"
    }
  ]
};

router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, function (event) {

  // You can also optionally provide a handler like this which will be passed any events that occur on the bridge
  // You can use this for monitoring or logging, or to change the raw messages in-flight.
  // It can also be used for fine grained access control.

  if (event.type() === BridgeEvent.Type.SOCKET_CREATED) {
    console.log("A socket was created");
  }

  // This signals that it's ok to process the event
  event.complete(true);

}).handle);

// Serve the static resources
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

// Publish a message to the address "news-feed" every second
vertx.setPeriodic(1000, function (t) {
  vertx.eventBus().publish("news-feed", "news from the server!");
});
