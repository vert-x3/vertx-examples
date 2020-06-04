package io.vertx.example.web.jwt

import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.KeyStoreOptions
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.web.Router
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

    router.get("/api/newToken").handler({ ctx ->
      ctx.response().putHeader("Content-Type", "text/plain")
      ctx.response().end(jwt.generateToken(json {
        obj()
      }, JWTOptions(
        expiresInSeconds = 60)))
    })

    // protect the API
    router.route("/api/*").handler(JWTAuthHandler.create(jwt))

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
