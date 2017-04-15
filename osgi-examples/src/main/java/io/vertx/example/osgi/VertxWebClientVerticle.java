package io.vertx.example.osgi;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import java.util.logging.Logger;

/**
 * A component exposing itself as a verticle in the service registry, and creating a Web client to consume a web
 * page.
 */
@Component
@Provides
@Instantiate
public class VertxWebClientVerticle extends AbstractVerticle {

  private final static Logger LOGGER = Logger.getLogger("VertxWebClientVerticle");

  @Override
  public void start() throws Exception {
    WebClient client = WebClient.create(vertx);
    client.get(80, "perdu.com", "/")
      .rxSend()
      .map(HttpResponse::bodyAsString)
      .subscribe(
        s -> LOGGER.info("From web client: " + s),
        Throwable::printStackTrace
      );
  }
}
