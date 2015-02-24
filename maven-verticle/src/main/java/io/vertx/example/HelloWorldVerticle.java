package io.vertx.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class HelloWorldVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    // Create an HTTP server which simply returns "Hello World!" to each request.
    Vertx.vertx().createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
  }
}
