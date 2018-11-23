package io.vertx.example.web.sessions;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(CookieHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

    router.route().handler(routingContext -> {

      Session session = routingContext.session();

      Integer cnt = session.get("hitcount");
      cnt = (cnt == null ? 0 : cnt) + 1;

      session.put("hitcount", cnt);

      routingContext.response().putHeader("content-type", "text/html")
                               .end("<html><body><h1>Hitcount: " + cnt + "</h1></body></html>");
    });

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}
