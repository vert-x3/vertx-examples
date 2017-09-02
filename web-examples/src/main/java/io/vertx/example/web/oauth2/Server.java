package io.vertx.example.web.oauth2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GithubAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  private static final String CLIENT_ID = System.getenv("GH_BASIC_CLIENT_ID");
  private static final String CLIENT_SECRET = System.getenv("GH_BASIC_SECRET_ID");

  // In order to use a template we first need to create an engine
  private final HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create();

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  private boolean isAuthenticated(RoutingContext ctx) {
    return ctx.user() != null;
  }

  private void authenticate(RoutingContext ctx) {
    // we pass the client id to the template
    ctx.put("client_id", CLIENT_ID);
    // and now delegate to the engine to render it.
    engine.render(ctx, "views", "/index.hbs", res -> {
      if (res.succeeded()) {
        ctx.response()
          .putHeader("Content-Type", "text/html")
          .end(res.result());
      } else {
        ctx.fail(res.cause());
      }
    });
  }

  @Override
  public void start() throws Exception {
    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    final Router router = Router.router(vertx);
    // We need cookies, sessions and request bodies
    router.route().handler(CookieHandler.create());
    router.route().handler(BodyHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
    // Simple auth service which uses a GitHub to authenticate the user
    OAuth2Auth authProvider = GithubAuth.create(vertx, CLIENT_ID, CLIENT_SECRET);
    // We need a user session handler too to make sure the user is stored in the session between requests
    router.route().handler(UserSessionHandler.create(authProvider));
    // Entry point to the application, this will render a custom template.
    router.get("/").handler(ctx -> {
      if (!isAuthenticated(ctx)) {
        authenticate(ctx);
      } else {
        AccessToken user = (AccessToken) ctx.user();

        user.userInfo(res -> {
          if (res.failed()) {
            res.cause().printStackTrace();
            // request didn't succeed because the token was revoked so we
            // invalidate the token stored in the session and render the
            // index page so that the user can start the OAuth flow again
            ctx.session().destroy();
            authenticate(ctx);
          } else {
            // the request succeeded, so we check the list of current scopes
            JsonArray scopes = new JsonArray();
            JsonObject userInfo = res.result();

            if (userInfo.containsKey("X-OAuth-Scopes")) {
              for (String scope : userInfo.getString("X-OAuth-Scopes").split(", ")) {
                scopes.add(scope);
              }
            }

            if (scopes.contains("user:email")) {
              // fetch the user emails from the github API
              user.fetch("https://api.github.com/user/emails", res2 -> {
                if (res2.failed()) {
                  res2.cause().printStackTrace();
                  // request didn't succeed because the token was revoked so we
                  // invalidate the token stored in the session and render the
                  // index page so that the user can start the OAuth flow again
                  ctx.session().destroy();
                  authenticate(ctx);
                } else {
                  userInfo.put("private_emails", res2.result().jsonArray());

                  // we pass the client id to the template
                  ctx.put("userInfo", userInfo);

                  // and now delegate to the engine to render it.
                  engine.render(ctx, "views", "/advanced.hbs", res3 -> {
                    if (res3.succeeded()) {
                      ctx.response()
                        .putHeader("Content-Type", "text/html")
                        .end(res3.result());
                    } else {
                      ctx.fail(res3.cause());
                    }
                  });
                }
              });
            }

          }
        });
      }
    });
    // the callback url
    OAuth2AuthHandler oauth2Handler = OAuth2AuthHandler.create(authProvider, "http://localhost:8080/callback");
    oauth2Handler.setupCallback(router.route("/callback"));

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}

