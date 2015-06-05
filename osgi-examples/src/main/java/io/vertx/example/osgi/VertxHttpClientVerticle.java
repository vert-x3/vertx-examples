package io.vertx.example.osgi;

import io.vertx.core.AbstractVerticle;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import java.util.logging.Logger;

/**
 * A component exposing itself as a verticle in the service registry, and creating a HTTP client to consume a web
 * page.
 */
@Component
@Provides
@Instantiate
public class VertxHttpClientVerticle extends AbstractVerticle {

  private final static Logger LOGGER = Logger.getLogger("VertxHttpClientVerticle");

  @Override
  public void start() throws Exception {
    getVertx().createHttpClient().getNow("perdu.com", "/", response -> {
      response.bodyHandler(buffer -> LOGGER.info(buffer.toString("UTF-8")));
    });
  }
}
