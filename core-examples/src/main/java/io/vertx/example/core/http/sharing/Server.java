package io.vertx.example.core.http.sharing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.launcher.application.VertxApplication;

/**
 * An example illustrating the server sharing and round robin. The servers are identified using an id.
 * The HTTP Server Verticle is instantiated twice in the deployment options.
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() throws Exception {
    vertx.deployVerticle(
        "io.vertx.example.core.http.sharing.HttpServerVerticle",
        new DeploymentOptions().setInstances(2));
  }
}
