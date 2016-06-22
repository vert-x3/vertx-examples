var request = vertx.createHttpClient({
  "proxyOptions" : {
    "host" : "localhost",
    "port" : 8080
  }
}).put(8282, "localhost", "/", function (resp) {
  console.log("Got response " + resp.statusCode());
  resp.bodyHandler(function (body) {
    console.log("Got data " + body.toString("ISO-8859-1"));
  });
});

request.setChunked(true);

for (var i = 0; i < 10; i++) {
  request.write("client-chunk-" + i);
}

request.end();
