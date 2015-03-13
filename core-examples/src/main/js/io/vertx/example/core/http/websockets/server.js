vertx.createHttpServer().websocketHandler(function (ws) {
  ws.handler(ws.writeMessage)}).requestHandler(function (req) {
  if (req.uri() == "/") {
    req.response().sendFile("ws.html")};
}).listen(8080);
