
var server = vertx.createHttpServer({
});

server.requestHandler(function (req) {
  req.response().putHeader("content-type", "text/html").end("<html><body>" + "<h1>Hello from vert.x!</h1>" + "<p>version = " + req.version() + "</p>" + "</body></html>");
}).listen(8080);
