package io.vertx.example.core.http.sendfile;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SendFile extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", SendFile.class.getName());
  }

  @Override
  public void start() throws Exception {

    // In reality, it's highly recommend you use Vert.x-Web for applications like this.

    vertx.createHttpServer().requestHandler(req -> {
      String filename = null;
      if (req.path().equals("/")) {
        filename = "index.html";
      } else if (req.path().equals("/page1.html")) {
        filename = "page1.html";
      } else if (req.path().equals("/page2.html")) {
        filename = "page2.html";
      } else {
        req.response().setStatusCode(404).end();
      }
      if (filename != null) {
        req.response().sendFile("io/vertx/example/core/http/sendfile/" + filename);
      }
    }).listen(8080);
  }
}
