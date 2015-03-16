var client = vertx.createHttpClient();
var req = client.request('GET', 8080, "localhost", "/");
req.toObservable().subscribe(function (resp) {
  console.log("Got response " + resp.statusCode());
  resp.bodyHandler(function (body) {
    console.log("Got data " + body.toString("ISO-8859-1"));
  });
});
req.end();
