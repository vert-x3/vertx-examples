vertx.createHttpServer().webSocketHandler({ ws ->
  ws.handler(ws.&writeBinaryMessage)
}).requestHandler({ req ->
  if (req.uri() == "/") {
    req.response().sendFile("ws.html")}
}).listen(8080)
