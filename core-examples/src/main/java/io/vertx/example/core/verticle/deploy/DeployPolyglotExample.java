package io.vertx.example.core.verticle.deploy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class DeployPolyglotExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(DeployPolyglotExample.class);
  }

  @Override
  public void start() throws Exception {

    System.out.println("Main verticle has started, let's deploy A JS one...");

    // Deploy a verticle and don't wait for it to start,
    // the js verticle will use the noop2 npm module (which does nothing)
    // will invoke it and print a message
    vertx.deployVerticle("jsverticle.js");
  }
}
