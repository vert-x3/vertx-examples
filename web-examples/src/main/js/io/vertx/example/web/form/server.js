var Router = require("vertx-web-js/router");
var BodyHandler = require("vertx-web-js/body_handler");

var router = Router.router(vertx);

// Enable multipart form data parsing
router.route().handler(BodyHandler.create().handle);

router.route("/").handler(function (routingContext) {
  routingContext.response().putHeader("content-type", "text/html").end("<form action=\"/form\" method=\"post\">\n    <div>\n        <label for=\"name\">Enter your name:</label>\n        <input type=\"text\" id=\"name\" name=\"name\" />\n    </div>\n    <div class=\"button\">\n        <button type=\"submit\">Send</button>\n    </div></form>");
});

// handle the form
router.post("/form").handler(function (ctx) {
  ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "text/plain");
  // note the form attribute matches the html form element name.
  ctx.response().end("Hello " + ctx.request().getParam("name") + "!");
});

vertx.createHttpServer().requestHandler(router.accept).listen(8080);
