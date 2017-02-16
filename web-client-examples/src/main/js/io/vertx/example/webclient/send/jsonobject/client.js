var WebClient = require("vertx-web-client-js/web_client");

var client = WebClient.create(vertx);

var user = {
  "firstName" : "Dale",
  "lastName" : "Cooper",
  "male" : true
};

client.put(8080, "localhost", "/").sendJson(user, function (ar, ar_err) {
  if (ar_err == null) {
    var response = ar;
    console.log("Got HTTP response with status " + response.statusCode());
  } else {
    ar_err.printStackTrace();
  }
});
