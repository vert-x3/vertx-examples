
// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

var options = {
  "ssl" : true,
  "useAlpn" : true,
  "openSslEngineOptions" : {
  },
  "protocolVersion" : "HTTP_2",
  "trustAll" : true
};

var client = vertx.createHttpClient(options);

var request = client.get(8443, "localhost", "/", function (resp) {
  console.log("Got response " + resp.statusCode() + " with protocol " + resp.version());
  resp.bodyHandler(function (body) {
    console.log("Got data " + body.toString("ISO-8859-1"));
  });
});

// Set handler for server side push
request.pushHandler(function (pushedReq) {
  console.log("Receiving pushed content");
  pushedReq.handler(function (pushedResp) {
    pushedResp.bodyHandler(function (body) {
      console.log("Got pushed data " + body.toString("ISO-8859-1"));
    });
  });
});

request.end();
