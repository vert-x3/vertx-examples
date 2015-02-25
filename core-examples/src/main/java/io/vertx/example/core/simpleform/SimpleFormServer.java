package io.vertx.example.core.simpleform;

import io.vertx.core.AbstractVerticle;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleFormServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      if (req.uri().equals("/")) {
        // Serve the index page
        req.response().sendFile("simpleform/index.html");
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
