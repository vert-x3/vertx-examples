
console.log("cwd:" + new java.io.File(".").getAbsolutePath());

var Router = require("vertx-apex-js/router");

var router = Router.router(vertx);

router.route().handler(function(routingContext) {
  routingContext.response().putHeader("content-type", "text/plain").end("Hello World from JS!");
});

var server = vertx.createHttpServer();

server.requestHandler(router.accept).listen(8080);
