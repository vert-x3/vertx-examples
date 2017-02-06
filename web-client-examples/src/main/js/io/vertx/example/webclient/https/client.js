var WebClient = require("vertx-web-client-js/web_client");

// Create the web client and enable SSL/TLS with a trust store
var client = WebClient.create(vertx, {
  "ssl" : true,
  "trustStoreOptions" : {
    "path" : "client-truststore.jks",
    "password" : "wibble"
  }
});

client.get(8443, "localhost", "/").send(function (ar, ar_err) {
  if (ar_err == null) {
    var response = ar;
    console.log("Got HTTP response with status " + response.statusCode());
  } else {
    ar_err.printStackTrace();
  }
});
