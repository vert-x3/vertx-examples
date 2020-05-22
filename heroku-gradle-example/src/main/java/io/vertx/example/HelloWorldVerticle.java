package io.vertx.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class HelloWorldVerticle extends AbstractVerticle {

  @Override
  public void start() {
    // Create an HTTP server which simply returns "Hello World!" to each request.
    vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(Integer.getInteger("server.port"), System.getProperty("http.address", "0.0.0.0"));
  }
}
