package io.vertx.example.virtualthreads.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.virtualthreads.await.VirtualThreadOptions;

import static io.vertx.virtualthreads.await.Async.await;

public class WebClientExample extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(WebClientExample.class, new DeploymentOptions()
        .setWorker(true)
        .setWorkerOptions(new VirtualThreadOptions()))
      .toCompletionStage()
      .toCompletableFuture()
      .get();
  }

  @Override
  public void start() {
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(request -> {
      request.response().end("Hello World");
    });
    await(server.listen(8080, "localhost"));

    // Make a simple HTTP request
    WebClient client = WebClient.create(vertx);
    HttpResponse<Buffer> resp = await(client
      .get(8080, "localhost", "/")
      .expect(ResponsePredicate.SC_OK)
      .send());
    int status = resp.statusCode();
    Buffer body = resp.body();
    System.out.println("Got response status=" + status + " body='" + body + "'");
  }
}
