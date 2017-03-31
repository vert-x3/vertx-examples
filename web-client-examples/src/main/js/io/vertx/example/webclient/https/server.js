
// Start an SSL/TLS http server
vertx.createHttpServer({
  "keyStoreOptions" : {
    "path" : "server-keystore.jks",
    "password" : "wibble"
  },
  "ssl" : true
}).requestHandler(function (req) {

  req.response().end();

}).listen(8443, function (listenResult, listenResult_err) {
  if (listenResult_err != null) {
    console.log("Could not start HTTP server");
    listenResult_err.printStackTrace();
  } else {
    console.log("Server started");
  }
});
