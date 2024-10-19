package io.vertx.example.virtualthreads;

import io.vertx.core.*;
import io.vertx.core.http.*;

public class HttpClientExample extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(HttpClientExample.class, new DeploymentOptions().setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
      .await();
  }

  @Override
  public void start() {
    var server = vertx.createHttpServer();
    server.requestHandler(request -> {
      request.response().end("Hello World");
    });
    server.listen(8080, "localhost").await();

    // Make a simple HTTP request
    var client = vertx.createHttpClient();
    var req = client.request(HttpMethod.GET, 8080, "localhost", "/").await();
    var resp = req.send().await();
    var status = resp.statusCode();
    var body = resp.body().await();
    System.out.println("Got response status=" + status + " body='" + body + "'");
  }
}
