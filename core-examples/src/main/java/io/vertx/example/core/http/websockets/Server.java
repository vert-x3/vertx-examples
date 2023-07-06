package io.vertx.example.core.http.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().webSocketHandler(ws -> ws.handler(ws::writeBinaryMessage)).requestHandler(req -> {
      if (req.uri().equals("/")) req.response().sendFile("io/vertx/example/core/http/websockets/ws.html");
    }).listen(8080);
  }
}
