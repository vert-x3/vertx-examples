var client = vertx.createHttpClient({
});
vertx.createHttpServer().requestHandler(function (req) {
  console.log("Proxying request: " + req.uri());
  var c_req = client.request(req.method(), 8282, "localhost", req.uri(), function (c_res) {
    console.log("Proxying response: " + c_res.statusCode());
    req.response().setChunked(true);
    req.response().setStatusCode(c_res.statusCode());
    req.response().headers().setAll(c_res.headers());
    c_res.handler(function (data) {
      console.log("Proxying response body: " + data.toString("ISO-8859-1"));
      req.response().write(data);
    });
    c_res.endHandler(function (v) {
      req.response().end()
    });
  });
  c_req.setChunked(true);
  c_req.headers().setAll(req.headers());
  req.handler(function (data) {
    console.log("Proxying request body " + data.toString("ISO-8859-1"));
    c_req.write(data);
  });
  req.endHandler(function (v) {
    c_req.end()
  });
}).listen(8080);
