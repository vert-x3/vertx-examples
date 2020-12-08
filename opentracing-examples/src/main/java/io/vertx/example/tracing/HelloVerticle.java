package io.vertx.example.tracing;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;

public class HelloVerticle extends AbstractVerticle {

  private WebClient client;

  @Override
  public void start() {
    client = WebClient.create(vertx);
    vertx.createHttpServer().requestHandler(req -> {
      client
        .get(8082, "localhost", "/")
        .expect(ResponsePredicate.SC_OK)
        .expect(ResponsePredicate.contentType("text/plain"))
        .send()
        .onSuccess(resp -> {
          req.response().end("Hello, here is a joke for you \"" + resp.bodyAsString() + "\"");
        })
        .onFailure(failure -> {
          failure.printStackTrace();
          req.response().end("Hello, sorry no joke for you today");
        });
    }).listen(8081);
  }
}
