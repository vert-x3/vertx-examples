package io.vertx.example.shell.run_service_http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.HttpTermOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RunShell extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher launcher = new Launcher() {
      @Override
      public void beforeStartingVertx(VertxOptions options) {
        options.setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
      }
    };
    launcher.dispatch(new String[]{"run", RunShell.class.getName()});
  }

  @Override
  public void start(Promise<Void> startPromise) {
    ShellService service = ShellService.create(vertx, new ShellServiceOptions().
      setHttpOptions(
        new HttpTermOptions().
          setHost("localhost").
          setPort(8080).
          setAuthOptions(new JsonObject()
            .put("provider", "properties")
            .put("config", new JsonObject()
              .put("file", "io/vertx/example/shell/run_service_http/auth.properties")))));
    service.start().onComplete(startPromise);
  }
}
