var Pump = require("vertx-js/pump");
var req = vertx.createHttpClient({
}).put(8080, "localhost", "/someurl", function (resp) {
  console.log("Response " + resp.statusCode());
});
var filename = "upload.txt";
var fs = vertx.fileSystem();

fs.props(filename, function (ares, ares_err) {
  var props = ares;
  console.log("props is " + props);
  var size = props.size();
  req.headers().set("content-length", Java.type("java.lang.String").valueOf(size));
  fs.open(filename, {
  }, function (ares2, ares2_err) {
    var file = ares2;
    var pump = Pump.pump(file, req);
    file.endHandler(function (v) {
      req.end();
    });
    pump.start();
  });
});


