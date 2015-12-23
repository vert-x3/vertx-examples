package io.vertx.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

/**
 * @author <a href="http://alexvetter.de">Alexander Vetter</a>
 */
public class HelloWorldVerticle extends AbstractVerticle {

  private HttpServer httpServer;
  
  @Override
  public void start(Future fut) {
    // Create an HTTP server...
    httpServer = vertx.createHttpServer();
    
    // ... which simply returns "Hello World!" to each request.
    httpServer.requestHandler( req -> {
      req.response().end("Hello World!");
    });
    httpServer.listen(8080, res -> fut.complete());
  }

  @Override
  public void stop(Future fut) {
    httpServer.close( res -> fut.complete());
  }
}
