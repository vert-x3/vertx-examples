var Pump = require("vertx-js/pump");
vertx.createHttpServer().requestHandler(function (req) {
  req.pause();
  var filename = Java.type("java.util.UUID").randomUUID() + ".uploaded";
  vertx.fileSystem().open(filename, {
  }, function (ares, ares_err) {
    var file = ares;
    var pump = Pump.pump(req, file);
    req.endHandler(function (v1) {
      file.close(function (v2, v2_err) {
        console.log("Uploaded to " + filename);
        req.response().end();
      });
    });
    pump.start();
    req.resume();
  });
}).listen(8080);
