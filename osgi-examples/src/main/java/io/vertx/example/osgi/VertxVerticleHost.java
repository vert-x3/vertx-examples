package io.vertx.example.osgi;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import org.apache.felix.ipojo.annotations.*;

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

  @Invalidate
  public void stop() {
    // We should unregister all deployed verticles.
  }

  @Bind(aggregate = true)
  public void bindVerticle(Verticle verticle) {
    LOGGER.info("Deploying verticle " + verticle);
    vertx.deployVerticle(verticle);
  }

  @Unbind(aggregate = true)
  public void unbindVerticle(Verticle verticle) {
    LOGGER.info("Undeploying verticle " + verticle);
    vertx.undeploy(verticle.getVertx().getOrCreateContext().deploymentID());
  }
}
