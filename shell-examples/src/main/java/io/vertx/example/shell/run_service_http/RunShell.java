package io.vertx.example.shell.run_service_http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.HttpTermOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RunShell extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(RunShell.class);
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    ShellService service = ShellService.create(vertx, new ShellServiceOptions().
        setHttpOptions(
            new HttpTermOptions().
                setHost("localhost").
                setPort(8080).
                setShiroAuthOptions(new ShiroAuthOptions().
                    setConfig(new JsonObject().put("properties_path", "auth.properties")))));
    service.start(ar -> {
      if (ar.succeeded()) {
        startFuture.succeeded();
      } else {
        startFuture.fail(ar.cause());
      }
    });
  }
}
