package io.vertx.example.virtualthreads;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.ext.web.client.WebClient;

import static io.vertx.core.Future.await;

public class WebClientExample extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(WebClientExample.class, new DeploymentOptions()
        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
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
    var client = WebClient.create(vertx);
    var resp = await(client
      .get(8080, "localhost", "/")
      .send()
      .expecting(HttpResponseExpectation.SC_OK));
    var status = resp.statusCode();
    var body = resp.body();
    System.out.println("Got response status=" + status + " body='" + body + "'");
  }
}
