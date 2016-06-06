
var options = {
  "protocolVersion" : 'HTTP_2'
};

vertx.createHttpClient(options).getNow(8080, "localhost", "/", function (resp) {
  console.log("Got response " + resp.statusCode() + " with protocol " + resp.version());
  resp.bodyHandler(function (body) {
    console.log("Got data " + body.toString("ISO-8859-1"));
  });
});
