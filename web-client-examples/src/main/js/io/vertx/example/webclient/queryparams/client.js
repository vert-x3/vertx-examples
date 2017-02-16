var WebClient = require("vertx-web-client-js/web_client");

var client = WebClient.create(vertx);

client.get(8080, "localhost", "/").addQueryParam("firstName", "Dale").addQueryParam("lastName", "Cooper").addQueryParam("male", "true").send(function (ar, ar_err) {
  if (ar_err == null) {
    var response = ar;
    console.log("Got HTTP response with status " + response.statusCode());
  } else {
    ar_err.printStackTrace();
  }
});
