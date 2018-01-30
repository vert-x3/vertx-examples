package io.vertx.example.reactivex.web.backpressure;

import io.vertx.example.util.Runner;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientResponse;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tomasz.michalak
 */
public class Server extends AbstractVerticle {

  private static final int NUMBER_OF_REQUESTS = 1000;
  private static final int N_THREADS = 100;

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) throws InterruptedException {
    Runner.runExample(io.vertx.example.reactivex.web.backpressure.Server.class);
  }

  @Override
  public void start() throws Exception {

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

    server.rxListen(8080).subscribe(res -> generateRequests());
  }

  private void generateRequests() throws InterruptedException {
    AtomicInteger ok = new AtomicInteger(0);
    AtomicInteger fail = new AtomicInteger(0);

    List<Callable<HttpClient>> tasks = Collections.nCopies(NUMBER_OF_REQUESTS,
      () -> vertx.createHttpClient()
        .getNow(8080, "localhost", "/", resp ->
          log(resp, resp.statusCode() == 200 ? ok.incrementAndGet() : ok.get(),
            resp.statusCode() == 503 ? fail.incrementAndGet() : fail.get())
        ));
    Executors.newFixedThreadPool(N_THREADS).invokeAll(tasks);

  }

  private void log(HttpClientResponse response, int ok, int fail) {
    System.out.println(response.statusCode() + ", ok=" + ok + ", fail=" + fail);
  }

}
