vertx.createHttpClient().getNow(8080, "localhost", "/", function (resp) {
  console.log("Got response " + resp.statusCode());
  resp.bodyHandler(function (body) {
    console.log("Got data " + body.toString("ISO-8859-1"));
  });
});
