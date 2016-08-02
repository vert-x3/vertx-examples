package io.vertx.examples.resteasy.asyncresponse;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.resteasy.util.Runner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Main extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Main.class);
  }

  @Override
  public void start() throws Exception {
    vertx.deployVerticle(Server.class.getName());
    vertx.deployVerticle(ProductBackend.class.getName());
  }
}
