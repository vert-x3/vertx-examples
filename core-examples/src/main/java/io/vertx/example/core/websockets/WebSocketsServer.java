package io.vertx.example.core.websockets;

import io.vertx.core.AbstractVerticle;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class WebSocketsServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().websocketHandler(ws -> ws.handler(ws::writeMessage)).requestHandler(req -> {
      if (req.uri().equals("/")) req.response().sendFile("websockets/ws.html");
    }).listen(8080);
  }
}
