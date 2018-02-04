var Router = require("vertx-web-js/router");
var CorsHandler = require("vertx-web-js/cors_handler");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

var allowedHeaders = new (Java.type("java.util.HashSet"))();
allowedHeaders.add("x-requested-with");
allowedHeaders.add("Access-Control-Allow-Origin");
allowedHeaders.add("origin");
allowedHeaders.add("Content-Type");
allowedHeaders.add("accept");
allowedHeaders.add("X-PINGARUNER");

var allowedMethods = new (Java.type("java.util.HashSet"))();
allowedMethods.add('GET');
allowedMethods.add('POST');
allowedMethods.add('OPTIONS');
/*
 * these methods aren't necessary for this sample, 
 * but you may need them for your projects
 */
allowedMethods.add('DELETE');
allowedMethods.add('PATCH');
allowedMethods.add('PUT');

router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods).handle);

router.get("/access-control-with-get").handler(function (ctx) {
  var httpServerResponse = ctx.response();
  httpServerResponse.setChunked(true);
  var headers = ctx.request().headers();
  Array.prototype.forEach.call(headers.names(), function(key) {
    httpServerResponse.write(key + ": ");
    httpServerResponse.write(headers.get(key));
    httpServerResponse.write("<br>");
  });
  httpServerResponse.putHeader("Content-Type", "application/text").end("Success");
});

router.post("/access-control-with-post-preflight").handler(function (ctx) {
  var httpServerResponse = ctx.response();
  httpServerResponse.setChunked(true);
  var headers = ctx.request().headers();
  Array.prototype.forEach.call(headers.names(), function(key) {
    httpServerResponse.write(key + ": ");
    httpServerResponse.write(headers.get(key));
    httpServerResponse.write("<br>");
  });
  httpServerResponse.putHeader("Content-Type", "application/text").end("Success");
});

// Serve the static resources
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);
