package io.vertx.example.core.http.simpleform;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleFormServer extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SimpleFormServer.class);
  }

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      if (req.uri().equals("/")) {
        // Serve the index page
        req.response().sendFile("index.html");
      } else if (req.uri().startsWith("/form")) {
        req.response().setChunked(true);
        req.setExpectMultipart(true);
        req.endHandler((v) -> {
          for (String attr : req.formAttributes().names()) {
            req.response().write("Got attr " + attr + " : " + req.formAttributes().get(attr) + "\n");
          }
          req.response().end();
        });
      } else {
        req.response().setStatusCode(404).end();
      }
    }).listen(8080);
  }
}
