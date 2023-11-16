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
