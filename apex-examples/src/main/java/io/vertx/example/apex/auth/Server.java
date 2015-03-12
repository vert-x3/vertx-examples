package io.vertx.example.apex.auth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.apex.Router;
import io.vertx.ext.apex.handler.*;
import io.vertx.ext.apex.sstore.LocalSessionStore;
import io.vertx.ext.auth.AuthService;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.auth.shiro.ShiroAuthService;

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

    // We need cookies, sessions and request bodies
    router.route().handler(CookieHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
    router.route().handler(BodyHandler.create());

    // Simple auth service which uses a properties file for user/role info
    AuthService authService = ShiroAuthService.create(vertx, ShiroAuthRealmType.PROPERTIES, new JsonObject());

    // Any requests to URI starting '/private/' require login
    router.route("/private/*").handler(RedirectAuthHandler.create(authService, "/loginpage.html"));

    // Serve the static private pages from directory 'private'
    router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"));

    // Handles the actual login
    router.route("/loginhandler").handler(FormLoginHandler.create(authService));

    // Implement logout
    router.route("/logout").handler(context -> {
      context.session().logout();
      // Redirect back to the index page
      context.response().putHeader("location", "/").setStatusCode(302).end();
    });

    // Serve the non private static pages
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
