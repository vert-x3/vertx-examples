package io.vertx.example.shell.termcast;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.TermServer;

import java.awt.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TermCast extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher launcher = new Launcher() {
      @Override
      public void beforeStartingVertx(VertxOptions options) {
        options.setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true));
      }
    };
    launcher.dispatch(new String[]{"run", TermCast.class.getName()});
  }

  private TermServer termServer;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    termServer = TermServer.createTelnetTermServer(vertx, new TelnetTermOptions().setHost("localhost").setPort(3000).setInBinary(false));
    Robot robot = new Robot();
    termServer.termHandler(term -> {
      new ScreenCaster(vertx, robot, term).handle();
    });
    termServer.listen().onComplete(ar -> {
      if (ar.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }

  @Override
  public void stop() {
    termServer.close();
  }
}
