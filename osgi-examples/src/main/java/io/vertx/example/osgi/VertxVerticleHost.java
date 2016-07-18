package io.vertx.example.osgi;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.apache.felix.ipojo.annotations.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A component consuming all verticles exposed in the service registry and deploying them.
 */
@Component(immediate = true)
@Instantiate
public class VertxVerticleHost {

  private final static Logger LOGGER = Logger.getLogger("VertxVerticleHost");

  @Requires
  Vertx vertx;

  ConcurrentHashMap<Verticle, String> deploymentIds = new ConcurrentHashMap<>();

  @Invalidate
  public void stop() {
    // We should unregister all deployed verticles.
  }

  @Bind(aggregate = true)
  public void bindVerticle(Verticle verticle) {
    LOGGER.info("Deploying verticle " + verticle);
    TcclSwitch.executeWithTCCLSwitch(() -> {
      vertx.deployVerticle(verticle, ar -> {
        if (ar.succeeded()) {
          deploymentIds.put(verticle, ar.result());
        } else {
          LOGGER.log(Level.SEVERE, "Cannot deploy " + verticle, ar.cause());
        }
      });
    });
  }

  @Unbind(aggregate = true)
  public void unbindVerticle(Verticle verticle) {
    String id = deploymentIds.get(verticle);
    LOGGER.info("Undeploying verticle " + verticle + " (" + id + ")");
    if (id != null) {
      vertx.undeploy(id);
    }
  }
}
