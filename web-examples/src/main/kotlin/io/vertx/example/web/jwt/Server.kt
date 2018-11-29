package io.vertx.example.web.jwt

import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.jwt.JWTOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.core.json.*
import io.vertx.kotlin.ext.jwt.*

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

    // protect the API
    router.route("/api/*").handler(JWTAuthHandler.create(jwt, "/api/newToken"))

    // this route is excluded from the auth handler
    router.get("/api/newToken").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end(jwt.generateToken(json {
        obj()
      }, JWTOptions(
        expiresInSeconds = 60)))
    })

    // this is the secret API
    router.get("/api/protected").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end("a secret you should keep for yourself...")
    })

    // Serve the non private static pages
    router.route().handler(StaticHandler.create())

    vertx.createHttpServer().requestHandler(router).listen(8080)
  }
}
