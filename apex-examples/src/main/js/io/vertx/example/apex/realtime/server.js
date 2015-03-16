var Router = require("vertx-apex-js/router");
var SockJSHandler = require("vertx-apex-js/sock_js_handler");
var StaticHandler = require("vertx-apex-js/static_handler");

var router = Router.router(vertx);

// Allow outbound traffic to the news-feed address

var options = {
  "outboundPermitteds" : [
    {
      "address" : "news-feed"
    }
  ]
};

router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options).handle);

// Serve the static resources
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

// Publish a message to the address "news-feed" every second
vertx.setPeriodic(1000, function (t) {
  vertx.eventBus().publish("news-feed", "news from the server!")});
