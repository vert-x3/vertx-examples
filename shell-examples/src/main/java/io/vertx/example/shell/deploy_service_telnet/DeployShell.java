package io.vertx.example.shell.deploy_service_telnet;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DeployShell extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(DeployShell.class);
  }

  @Override
  public void start(Promise<Void> startPromise) {
    JsonObject options = new JsonObject().put("telnetOptions",
      new JsonObject().
        put("host", "localhost").
        put("port", 3000)
    );
    vertx.deployVerticle("service:io.vertx.ext.shell", new DeploymentOptions().setConfig(options), ar -> {
      if (ar.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }
}
