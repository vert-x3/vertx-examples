package io.vertx.example.virtualthreads;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.ext.web.client.WebClient;

public class WebClientExample extends AbstractVerticle {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(WebClientExample.class, new DeploymentOptions()
        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
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
    var client = WebClient.create(vertx);
    var resp = client
      .get(8080, "localhost", "/")
      .send()
      .expecting(HttpResponseExpectation.SC_OK)
      .await();
    var status = resp.statusCode();
    var body = resp.body();
    System.out.println("Got response status=" + status + " body='" + body + "'");
  }
}
