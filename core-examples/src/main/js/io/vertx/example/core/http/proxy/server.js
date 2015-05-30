
vertx.createHttpServer().requestHandler(function (req) {

  console.log("Got request " + req.uri());

  Array.prototype.forEach.call(req.headers().names(), function(name) {
    console.log(name + ": " + req.headers().get(name));
  });

  req.handler(function (data) {
    console.log("Got data " + data.toString("ISO-8859-1"))});

  req.endHandler(function (v) {
    // Now send back a response
    req.response().setChunked(true);

    for (var i = 0; i < 10; i++) {
      req.response().write("server-data-chunk-" + i);
    }

    req.response().end();
  });
}).listen(8282);

