var WebClient = require("vertx-web-client-js/web_client");
var BodyCodec = require("vertx-web-common-js/body_codec");

var client = WebClient.create(vertx);

client.get(8080, "localhost", "/").as(BodyCodec.jsonObject()).send(function (ar, ar_err) {
  if (ar_err == null) {
    var response = ar;
    console.log("Got HTTP response body");
    console.log(JSON.stringify(response.body()));
  } else {
    ar_err.printStackTrace();
  }
});
