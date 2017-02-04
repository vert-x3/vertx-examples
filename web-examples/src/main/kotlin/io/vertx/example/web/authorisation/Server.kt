package io.vertx.example.web.authorisation

import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.ext.auth.jwt.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    // Create a JWT Auth Provider
    var jwt = JWTAuth.create(vertx, json {
      obj("keyStore" to obj(
          "type" to "jceks",
          "path" to "keystore.jceks",
          "password" to "secret"
        ))
    })

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
        expiresInSeconds = 60L,
        permissions = authorities)))
    })

    // protect the API (any authority is allowed)
    router.route("/api/protected").handler(JWTAuthHandler.create(jwt))

    router.get("/api/protected").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is not defcon!")
    })

    // protect the API (defcon1 authority is required)
    router.route("/api/protected/defcon1").handler(JWTAuthHandler.create(jwt).addAuthority("defcon1"))

    router.get("/api/protected/defcon1").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is defcon1!")
    })

    // protect the API (defcon2 authority is required)
    router.route("/api/protected/defcon2").handler(JWTAuthHandler.create(jwt).addAuthority("defcon2"))

    router.get("/api/protected/defcon2").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is defcon2!")
    })

    // protect the API (defcon3 authority is required)
    router.route("/api/protected/defcon3").handler(JWTAuthHandler.create(jwt).addAuthority("defcon3"))

    router.get("/api/protected/defcon3").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("this secret is defcon3!")
    })

    // Serve the non private static pages
    router.route().handler(StaticHandler.create())

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
