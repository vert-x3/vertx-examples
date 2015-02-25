package io.vertx.example.core.embed;

import io.vertx.core.Vertx;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class EmbeddedServer {

  public static void main(String[] args) {
    // Create an HTTP server which simply returns "Hello World!" to each request.
    Vertx.vertx().createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
  }
}
