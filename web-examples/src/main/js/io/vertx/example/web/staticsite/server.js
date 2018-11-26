var Router = require("vertx-web-js/router");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Serve the static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.handle).listen(8080);

console.log("Server is started");

