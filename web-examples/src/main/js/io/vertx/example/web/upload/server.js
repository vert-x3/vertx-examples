var Router = require("vertx-web-js/router");
var BodyHandler = require("vertx-web-js/body_handler");

var router = Router.router(vertx);

// Enable multipart form data parsing
router.route().handler(BodyHandler.create().setUploadsDirectory("uploads").handle);

router.route("/").handler(function (routingContext) {
  routingContext.response().putHeader("content-type", "text/html").end("<form action=\"/form\" method=\"post\" enctype=\"multipart/form-data\">\n    <div>\n        <label for=\"name\">Select a file:</label>\n        <input type=\"file\" name=\"file\" />\n    </div>\n    <div class=\"button\">\n        <button type=\"submit\">Send</button>\n    </div></form>");
});

// handle the form
router.post("/form").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/plain");

  ctx.response().setChunked(true);

  Array.prototype.forEach.call(ctx.fileUploads(), function(f) {
    console.log("f");
    ctx.response().write("Filename: " + f.fileName());
    ctx.response().write("\n");
    ctx.response().write("Size: " + f.size());
  });

  ctx.response().end();
});

vertx.createHttpServer().requestHandler(router.handle).listen(8080);
