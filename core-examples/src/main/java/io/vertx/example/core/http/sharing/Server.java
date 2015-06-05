package io.vertx.example.core.http.sharing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.example.util.Runner;

/**
 * An example illustrating the server sharing and round robin. The servers are identified using an id.
 * The HTTP Server Verticle is instantiated twice in the deployment options.
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    getVertx().deployVerticle(
        "io.vertx.example.core.http.sharing.HttpServerVerticle",
        new DeploymentOptions().setInstances(2));
  }
}
