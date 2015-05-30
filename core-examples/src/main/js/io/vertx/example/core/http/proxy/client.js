var request = vertx.createHttpClient({
}).put(8080, "localhost", "/", function (resp) {
  console.log("Got response " + resp.statusCode());
  resp.bodyHandler(function (body) {
    console.log("Got data " + body.toString("ISO-8859-1"))});
});

request.setChunked(true);

for (var i = 0; i < 10; i++) {
  request.write("client-chunk-" + i);
}

request.end();
