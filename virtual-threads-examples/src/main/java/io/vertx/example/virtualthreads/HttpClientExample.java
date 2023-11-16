package io.vertx.example.virtualthreads;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;

import static io.vertx.core.Future.await;

public class HttpClientExample extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(HttpClientExample.class, new DeploymentOptions().setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
      .toCompletionStage()
      .toCompletableFuture()
      .get();
  }

  @Override
  public void start() {
    var server = vertx.createHttpServer();
    server.requestHandler(request -> {
      request.response().end("Hello World");
    });
    await(server.listen(8080, "localhost"));

    // Make a simple HTTP request
    var client = vertx.createHttpClient();
    var req = await(client.request(HttpMethod.GET, 8080, "localhost", "/"));
    var resp = await(req.send());
    var status = resp.statusCode();
    var body = await(resp.body());
    System.out.println("Got response status=" + status + " body='" + body + "'");
  }
}
