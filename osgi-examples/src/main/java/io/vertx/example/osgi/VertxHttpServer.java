package io.vertx.example.osgi;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.apache.felix.ipojo.annotations.*;

import java.util.logging.Logger;

import static io.vertx.example.osgi.TcclSwitch.executeWithTCCLSwitch;

/**
 * A component creating a Vert.x HTTP Server.
 */
@Component(immediate = true)
@Instantiate
public class VertxHttpServer {

  private final static Logger LOGGER = Logger.getLogger("VertxHttpServer");

  @Requires
  Vertx vertx;

  private HttpServer server;

  @Validate
  public void start() throws Exception {
    LOGGER.info("Creating vertx HTTP server");
    HttpServer server = executeWithTCCLSwitch(() -> vertx.createHttpServer());
    server.requestHandler((r) -> {
      r.response().end("Hello from OSGi !");
    }).listen(8080);
  }

  @Invalidate
  public void stop() {
     if (server != null) {
       server.close();
     }
  }


}
