var WebClient = require("vertx-web-client-js/web_client");
var MultiMap = require("vertx-js/multi_map");

var client = WebClient.create(vertx);

var form = MultiMap.caseInsensitiveMultiMap();
form.add("firstName", "Dale");
form.add("lastName", "Cooper");
form.add("male", "true");

client.post(8080, "localhost", "/").sendForm(form, function (ar, ar_err) {
  if (ar_err == null) {
    var response = ar;
    console.log("Got HTTP response with status " + response.statusCode());
  } else {
    ar_err.printStackTrace();
  }
});
