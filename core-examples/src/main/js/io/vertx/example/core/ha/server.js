vertx.createHttpServer().requestHandler(function (req) {
  var name = Java.type("java.lang.management.ManagementFactory").getRuntimeMXBean().getName();
  req.response().end("Happily served by " + name);
}).listen(8080);
