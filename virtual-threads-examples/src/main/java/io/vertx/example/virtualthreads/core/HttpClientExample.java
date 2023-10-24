package io.vertx.example.virtualthreads.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.virtualthreads.await.VirtualThreadOptions;

import static io.vertx.virtualthreads.await.Async.await;

public class HttpClientExample extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(HttpClientExample.class, new DeploymentOptions()
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
    HttpClient client = vertx.createHttpClient();
    HttpClientRequest req = await(client.request(HttpMethod.GET, 8080, "localhost", "/"));
    HttpClientResponse resp = await(req.send());
    int status = resp.statusCode();
    Buffer body = await(resp.body());
    System.out.println("Got response status=" + status + " body='" + body + "'");
  }
}
