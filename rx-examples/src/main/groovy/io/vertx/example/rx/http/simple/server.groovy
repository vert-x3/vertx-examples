def server = vertx.createHttpServer()
server.requestStream().toObservable().subscribe({ req ->
  req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>")
})
server.listen(8080)
