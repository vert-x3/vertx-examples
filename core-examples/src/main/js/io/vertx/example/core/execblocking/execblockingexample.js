
vertx.createHttpServer().requestHandler(function (request) {

  // Let's say we have to call a blocking API (e.g. JDBC) to execute a query for each
  // request. We can't do this directly or it will block the event loop
  // But you can do this using executeBlocking:

  vertx.executeBlocking(function (future) {

    // Do the blocking operation in here

    // Imagine this was a call to a blocking API to get the result
    try {
      Java.type("java.lang.Thread").sleep(500);
    } catch(err) {
    }

    var result = "armadillos!";

    future.complete(result);

  }, function (res, res_err) {

    if (res_err == null) {

      request.response().putHeader("content-type", "text/plain").end(res);

    } else {
      res_err.printStackTrace();
    }
  });

}).listen(8080);

