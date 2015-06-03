vertx.createHttpServer().websocketHandler({ ws ->
  ws.handler(ws.&writeMessage)
}).requestHandler({ req ->
  if (req.uri() == "/") {
    req.response().sendFile("ws.html")}
}).listen(8080)
