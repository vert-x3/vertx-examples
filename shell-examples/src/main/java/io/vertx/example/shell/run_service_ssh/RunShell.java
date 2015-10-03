package io.vertx.example.shell.run_service_ssh;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.example.util.Runner;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.auth.ShiroAuthOptions;
import io.vertx.ext.shell.net.SSHOptions;

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
        setSSHOptions(
            new SSHOptions().
                setHost("localhost").
                setPort(3000).
                setKeyPairOptions(new JksOptions().
                    setPath("keystore.jks").
                    setPassword("wibble")).
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
