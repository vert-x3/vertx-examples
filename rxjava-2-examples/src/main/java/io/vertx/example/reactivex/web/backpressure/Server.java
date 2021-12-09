package io.vertx.example.reactivex.web.backpressure;

import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.*;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tomasz.michalak
 */
public class Server extends AbstractVerticle {

  private static final int NUMBER_OF_REQUESTS = 1000;
  private static final int N_THREADS = 100;
  private static final int PORT = 8080;

  private static AtomicInteger allCounter = new AtomicInteger();
  private static AtomicInteger successCounter = new AtomicInteger();
  private static AtomicInteger failureCounter = new AtomicInteger();

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
        router.handle(req);
      });

    server.rxListen(PORT).subscribe(res -> generateRequests());
  }

  private void generateRequests() {
    ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
    for (int i = 0; i < NUMBER_OF_REQUESTS; i++) {
      executorService.execute(() -> {
        HttpClient client = vertx.createHttpClient();
        client.rxRequest(HttpMethod.GET, PORT, "localhost", "/")
          .flatMap(HttpClientRequest::rxSend)
          .subscribe(
            resp -> log(resp, successCounter(resp), failureCounter(resp)),
            Throwable::printStackTrace);
      });
    }
  }

  private int failureCounter(HttpClientResponse resp) {
    return resp.statusCode() == 503 ? failureCounter.incrementAndGet() : failureCounter.get();
  }

  private int successCounter(HttpClientResponse resp) {
    return resp.statusCode() == 200 ? successCounter.incrementAndGet() : successCounter.get();
  }

  private void log(HttpClientResponse response, int success, int failure) {
    System.out.println(String
      .format(
        "Response status code: [%s] , number of all responses [%s], number of correct responses [%s], number of dropped responses [%s]",
        response.statusCode(), allCounter.incrementAndGet(), success, failure));
  }

}
