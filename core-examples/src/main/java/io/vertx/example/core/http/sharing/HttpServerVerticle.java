package io.vertx.example.core.http.sharing;

import io.vertx.core.AbstractVerticle;

/**
 * A very simple HTTP server returning it's 'id' in the response.
 */
public class HttpServerVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
          .putHeader("content-type", "text/html")
          .end("<html><body><h1>Hello from " +  this + "</h1></body></html>");
    }).listen(8080);
  }
}

