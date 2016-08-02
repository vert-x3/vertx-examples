
var server = vertx.createHttpServer({
  "useAlpn" : true,
  "ssl" : true,
  "pemKeyCertOptions" : {
    "keyPath" : "server-key.pem",
    "certPath" : "server-cert.pem"
  }
});

server.requestHandler(function (req) {
  var path = req.path();
  var resp = req.response();
  if ("/" == path) {
    resp.push('GET', "/script.js", function (ar, ar_err) {
      if (ar_err == null) {
        console.log("sending push");
        var pushedResp = ar;
        pushedResp.sendFile("script.js");
      } else {
        // Sometimes Safari forbids push : "Server push not allowed to opposite endpoint."
      }
    });

    resp.sendFile("index.html");
  } else if ("/script.js" == path) {
    resp.sendFile("script.js");
  } else {
    console.log("Not found " + path);
    resp.setStatusCode(404).end();
  }
});

server.listen(8443, "localhost", function (ar, ar_err) {
  if (ar_err == null) {
    console.log("Server started");
  } else {
    ar_err.printStackTrace();
  }
});
