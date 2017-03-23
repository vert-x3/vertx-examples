var WebClient = require("vertx-web-client-js/web_client");

var filename = "upload.txt";
var fs = vertx.fileSystem();

var client = WebClient.create(vertx);

fs.props(filename, function (ares, ares_err) {
  var props = ares;
  console.log("props is " + props);
  var size = props.size();

  var req = client.put(8080, "localhost", "/");
  req.putHeader("content-length", "" + size);

  fs.open(filename, {
  }, function (ares2, ares2_err) {
    req.sendStream(ares2, function (ar, ar_err) {
      if (ar_err == null) {
        var response = ar;
        console.log("Got HTTP response with status " + response.statusCode());
      } else {
        ar_err.printStackTrace();
      }
    });
  });
});
