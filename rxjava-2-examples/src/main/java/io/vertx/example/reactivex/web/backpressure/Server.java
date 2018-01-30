package io.vertx.example.reactivex.web.backpressure;

import io.vertx.core.Future;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

/**
 * @author tomasz.michalak
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) throws InterruptedException {
    Runner.runExample(io.vertx.example.reactivex.web.backpressure.Server.class);
  }

  @Override
  public void start(Future<Void> fut) throws Exception {

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route().handler(req -> req.response().putHeader("content-type", "text/html")
      .end("<html><body><h1>Hello from vert.x!</h1></body></html>"));

    HttpServer server = vertx.createHttpServer();
    server.requestStream()
      .toFlowable()
      .map(HttpServerRequest::pause)
      .onBackpressureDrop(req -> req.response().setStatusCode(503).end())
      .observeOn(RxHelper.scheduler(vertx.getDelegate()))
      .subscribe(req -> {
        req.resume();
        router.accept(req);
      });

    server.rxListen(8080).subscribe(res -> fut.complete(), onError -> fut.fail(onError.getCause()));
  }

}
