var WebClient = require("vertx-web-client-js/web_client");
var Buffer = require("vertx-js/buffer");

var client = WebClient.create(vertx);

var body = Buffer.buffer("Hello World");

client.put(8080, "localhost", "/").sendBuffer(body, function (ar, ar_err) {
  if (ar_err == null) {
    var response = ar;
    console.log("Got HTTP response with status " + response.statusCode());
  } else {
    ar_err.printStackTrace();
  }
});
