package io.vertx.example.kubernetes.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class ClientApplication extends AbstractVerticle {

  private WebClient client;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(ClientApplication.class.getName());
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.get("/").handler(this::invoke);

    // Retrieve the service discovery
    ServiceDiscovery.create(vertx, discovery ->
      // Retrieve a web client
      HttpEndpoint.getWebClient(discovery, svc -> svc.getName().equals("vertx-greeting"), ar -> {
        if (ar.failed()) {
          System.out.println("D'oh the service is not available");
        } else {
          client = ar.result();
          vertx.createHttpServer().requestHandler(router).listen(8080);
        }
      }));
  }

  private void invoke(RoutingContext rc) {
    client.get("/luke").as(BodyCodec.string()).send(ar -> {
      if (ar.failed()) {
        rc.response().end("Unable to call the greeting service: " + ar.cause().getMessage());
      } else {
        if (ar.result().statusCode() != 200) {
          rc.response().end("Unable to call the greeting service - received status:" + ar.result().statusMessage());
        } else {
          rc.response().end("Greeting service invoked: '" + ar.result().body() + "'");
        }
      }
    });
  }
}
