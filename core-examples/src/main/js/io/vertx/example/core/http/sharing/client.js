vertx.setPeriodic(1000, function (l) {
  vertx.createHttpClient().getNow(8080, "localhost", "/", function (resp) {
    resp.bodyHandler(function (body) {
      console.log(body.toString("ISO-8859-1"));
    });
  });
});
