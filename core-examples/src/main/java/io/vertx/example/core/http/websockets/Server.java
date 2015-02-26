package io.vertx.example.core.http.websockets;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @CodeTranslate
  @Override
  public void start() throws Exception {
    vertx.createHttpServer().websocketHandler(ws -> ws.handler(ws::writeMessage)).requestHandler(req -> {
      if (req.uri().equals("/")) req.response().sendFile("ws.html");
    }).listen(8080);
  }
}
