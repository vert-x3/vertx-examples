package io.vertx.example.shell.deploy_service_ssh;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DeployShell extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher launcher = new Launcher() {
      @Override
      public void beforeStartingVertx(VertxOptions options) {
        options.setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
      }
    };
    launcher.dispatch(new String[]{"run", DeployShell.class.getName()});
  }

  @Override
  public void start(Promise<Void> startPromise) {
    JsonObject options = new JsonObject().put("sshOptions",
      new JsonObject().
        put("host", "localhost").
        put("port", 3000).
        put("keyPairOptions", new JsonObject().
          put("path", "io/vertx/example/shell/deploy_service_ssh/keystore.jks").
          put("password", "wibble")).
        put("authOptions", new JsonObject()
          .put("provider", "properties")
          .put("config", new JsonObject()
            .put("file", "io/vertx/example/shell/deploy_service_ssh/auth.properties"))

        )
    );
    vertx.deployVerticle("service:io.vertx.ext.shell", new DeploymentOptions().setConfig(options)).onComplete(ar -> {
      if (ar.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }
}
