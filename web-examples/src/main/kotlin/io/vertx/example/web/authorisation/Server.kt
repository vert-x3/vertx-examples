package io.vertx.example.web.authorisation

import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.KeyStoreOptions
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.auth.jwt.authorization.JWTAuthorization
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.AuthorizationHandler
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.core.json.*
import io.vertx.kotlin.ext.auth.*
import io.vertx.kotlin.ext.auth.jwt.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    // Create a JWT Auth Provider
    var jwt = JWTAuth.create(vertx, JWTAuthOptions(
      keyStore = KeyStoreOptions(
        type = "jceks",
        path = "keystore.jceks",
        password = "secret")))

    // this route is excluded from the auth handler (it represents your login endpoint)
    router.get("/api/newToken").handler({ ctx ->

      var authorities = mutableListOf<Any?>()

      for (authority in ctx.request().params().getAll("authority")) {
        authorities.add(authority)
      }

      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end(jwt.generateToken(json {
        obj()
      }, JWTOptions(
        expiresInSeconds = 60,
        permissions = authorities)))
    })

    var authnHandler = JWTAuthHandler.create(jwt)
    var authzProvider = JWTAuthorization.create("permissions")

    // protect the API (any authority is allowed)
    router.route("/api/*").handler(authnHandler)

    router.get("/api/protected").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is not defcon!")
    })

    // protect the API (defcon1 authority is required)
    var defcon1Handler = AuthorizationHandler.create(PermissionBasedAuthorization.create("defcon1")).addAuthorizationProvider(authzProvider)
    router.route("/api/protected/defcon1").handler(defcon1Handler)

    router.get("/api/protected/defcon1").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is defcon1!")
    })

    // protect the API (defcon2 authority is required)
    var defcon2Handler = AuthorizationHandler.create(PermissionBasedAuthorization.create("defcon2")).addAuthorizationProvider(authzProvider)
    router.route("/api/protected/defcon2").handler(defcon2Handler)

    router.get("/api/protected/defcon2").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is defcon2!")
    })

    // protect the API (defcon3 authority is required)
    var defcon3Handler = AuthorizationHandler.create(PermissionBasedAuthorization.create("defcon3")).addAuthorizationProvider(authzProvider)
    router.route("/api/protected/defcon3").handler(defcon3Handler)

    router.get("/api/protected/defcon3").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is defcon3!")
    })

    // Serve the non private static pages
    router.route().handler(StaticHandler.create())

    vertx.createHttpServer().requestHandler(router).listen(8080)
  }
}
