var Vertx = require("vertx-js/vertx");
// Create an HTTP server which simply returns "Hello World!" to each request.
Vertx.vertx().createHttpServer().requestHandler(function (req) {
  req.response().end("Hello World!")}).listen(8080);
