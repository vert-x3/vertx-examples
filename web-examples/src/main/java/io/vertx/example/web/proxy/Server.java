package io.vertx.example.web.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.ext.auth.properties.PropertyFileAuthentication;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.proxy.handler.ProxyHandler;
import io.vertx.httpproxy.HttpProxy;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start(Promise<Void> start) throws Exception {
    // deploy a dummy HTTP server that will be our backend
    vertx.deployVerticle(new Backend())
      .onFailure(start::fail)
      .onSuccess(id -> {

        Router router = Router.router(vertx);

        router
          // any request to "/foo" will in fact return the backend "/foo"
          .get("/foo")
          .handler(ProxyHandler.create(
            // create a reverse proxy
            HttpProxy.reverseProxy(vertx.createHttpClient())
              // to the dummy backend
              .origin(7070, "localhost")));

        // Example #2 (Security before Proxying)

        // Simple auth service which uses a properties file for user/role info
        PropertyFileAuthentication authn = PropertyFileAuthentication.create(vertx, "vertx-users.properties");
        AuthenticationHandler basicAuthHandler = BasicAuthHandler.create(authn);

        // All requests to paths starting with '/private/' will be protected
        router
          .get("/private")
          // the security is now handled before the proxy call executes
          .handler(basicAuthHandler)
          // any request to "/private" will in fact return the backend "/private"
          .handler(ProxyHandler.create(
            // create a reverse proxy
            HttpProxy.reverseProxy(vertx.createHttpClient())
              // to the dummy backend
              .origin(7070, "localhost")));

        router
          .route()
          .respond(ctx -> Future.succeededFuture("Hello World!"));

        vertx.createHttpServer()
          .requestHandler(router)
          .listen(8080)
          .onFailure(start::fail)
          .onSuccess(r -> {
            System.out.println("Open http://127.0.0.1:8080/foo");
            System.out.println("Protected (tim:sausages) http://127.0.0.1:8080/private");
            start.complete();
          });
      });
  }
}
