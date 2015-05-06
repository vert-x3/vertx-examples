
// In reality it's highly recommend you use Apex for applications like this.

vertx.createHttpServer().requestHandler(function (req) {
  var filename = null;
  if (req.path() == "/") {
    filename = "index.html";
  } else if (req.path() == "/page1.html") {
    filename = "page1.html";
  } else if (req.path() == "/page2.html") {
    filename = "page2.html";
  } else {
    req.response().setStatusCode(404).end();
  };
  if (filename !== null) {
    req.response().sendFile(filename);
  };
}).listen(8080);
