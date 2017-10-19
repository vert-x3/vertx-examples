package io.vertx.example.web.oauth2

import io.vertx.ext.auth.oauth2.AccessToken
import io.vertx.ext.auth.oauth2.providers.GithubAuth
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.OAuth2AuthHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.UserSessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.ext.web.templ.HandlebarsTemplateEngine

class Server : io.vertx.core.AbstractVerticle()  {
  var CLIENT_SECRET = "3155eafd33fc947e0fe9f44127055ce1fe876704"
  var CLIENT_ID = "57cdaa1952a3f4ee3df8"
  var engine = HandlebarsTemplateEngine.create()
  override fun start() {
    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    var router = Router.router(vertx)
    // We need cookies and sessions
    router.route().handler(CookieHandler.create())
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))
    // Simple auth service which uses a GitHub to authenticate the user
    var authProvider = GithubAuth.create(vertx, CLIENT_ID, CLIENT_SECRET)
    // We need a user session handler too to make sure the user is stored in the session between requests
    router.route().handler(UserSessionHandler.create(authProvider))
    // we now protect the resource under the path "/protected"
    router.route("/protected").handler(OAuth2AuthHandler.create(authProvider).setupCallback(router.route("/callback")).addAuthority("user:email"))
    // Entry point to the application, this will render a custom template.
    router.get("/").handler({ ctx ->
      // we pass the client id to the template
      ctx.put("client_id", CLIENT_ID)
      // and now delegate to the engine to render it.
      engine.render(ctx, "views", "/index.hbs", { res ->
        if (res.succeeded()) {
          ctx.response().putHeader("Content-Type", "text/html").end(res.result())
        } else {
          ctx.fail(res.cause())
        }
      })
    })
    // The protected resource
    router.get("/protected").handler({ ctx ->
      var user = ctx.user()
      // retrieve the user profile, this is a common feature but not from the official OAuth2 spec
      user.userInfo({ res ->
        if (res.failed()) {
          // request didn't succeed because the token was revoked so we
          // invalidate the token stored in the session and render the
          // index page so that the user can start the OAuth flow again
          ctx.session().destroy()
          ctx.fail(res.cause())
        } else {
          // the request succeeded, so we use the API to fetch the user's emails
          var userInfo = res.result()

          // fetch the user emails from the github API

          // the fetch method will retrieve any resource and ensure the right
          // secure headers are passed.
          user.fetch("https://api.github.com/user/emails", { res2 ->
            if (res2.failed()) {
              // request didn't succeed because the token was revoked so we
              // invalidate the token stored in the session and render the
              // index page so that the user can start the OAuth flow again
              ctx.session().destroy()
              ctx.fail(res2.cause())
            } else {
              userInfo.put("private_emails", res2.result().jsonArray())
              // we pass the client info to the template
              ctx.put("userInfo", userInfo)
              // and now delegate to the engine to render it.
              engine.render(ctx, "views", "/advanced.hbs", { res3 ->
                if (res3.succeeded()) {
                  ctx.response().putHeader("Content-Type", "text/html").end(res3.result())
                } else {
                  ctx.fail(res3.cause())
                }
              })
            }
          })
        }
      })
    })

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
