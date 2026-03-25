package io.vertx.example.core.http3.altsvc;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerConfig;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.ServerSSLOptions;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {

    HttpServer server =
      vertx.createHttpServer(new HttpServerConfig().setVersions(HttpVersion.HTTP_1_1, HttpVersion.HTTP_3), new ServerSSLOptions()
        .setKeyCertOptions(new PemKeyCertOptions()
          .setKeyPath("io/vertx/example/core/http2/simple/server-key.pem")
          .setCertPath("io/vertx/example/core/http2/simple/server-cert.pem")
        ));

    return server
      .requestHandler(req -> {
        if (req.version() == HttpVersion.HTTP_1_1) {
          req.response().putHeader(HttpHeaders.ALT_SVC, "h3=\":8443\"");
        }
        req.response().putHeader("content-type", "text/html").end("<html><body>" +
          "<h1>Hello from vert.x!</h1>" +
          "<p>version = " + req.version() + "</p>" +
          "</body></html>");
      })
      .listen(8443);
  }
}
