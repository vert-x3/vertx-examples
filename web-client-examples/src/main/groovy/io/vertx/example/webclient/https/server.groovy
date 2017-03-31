
// Start an SSL/TLS http server
vertx.createHttpServer([
  keyStoreOptions:[
    path:"server-keystore.jks",
    password:"wibble"
  ],
  ssl:true
]).requestHandler({ req ->

  req.response().end()

}).listen(8443, { listenResult ->
  if (listenResult.failed()) {
    println("Could not start HTTP server")
    listenResult.cause().printStackTrace()
  } else {
    println("Server started")
  }
})
