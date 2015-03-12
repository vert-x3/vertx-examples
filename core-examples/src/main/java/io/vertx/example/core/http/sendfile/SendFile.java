package io.vertx.example.core.http.sendfile;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SendFile extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SendFile.class);
  }

  @Override
  public void start() throws Exception {

    // In reality it's highly recommend you use Apex for applications like this.

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
        req.response().sendFile(filename);
      }
    }).listen(8080);
  }
}
