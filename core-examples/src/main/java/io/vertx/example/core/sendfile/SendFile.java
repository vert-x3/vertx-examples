package io.vertx.example.core.sendfile;

import io.vertx.core.AbstractVerticle;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SendFile extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      String filename = "sendfile/" + (req.uri().equals("/") ? "index.html" : "." + req.uri());
      System.out.println(filename);
      req.response().sendFile(filename);
    }).listen(8080);
  }
}
