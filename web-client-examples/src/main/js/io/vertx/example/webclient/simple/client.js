var WebClient = require("vertx-web-client-js/web_client");

var client = WebClient.create(vertx);

client.get(8080, "localhost", "/").send(function (ar, ar_err) {
  if (ar_err == null) {
    var response = ar;
    console.log("Got HTTP response with status " + response.statusCode() + " with data " + response.body().toString("ISO-8859-1"));
  } else {
    ar_err.printStackTrace();
  }
});
