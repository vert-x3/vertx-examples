import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.auth.jwt.JWTAuth
import io.vertx.groovy.ext.web.handler.JWTAuthHandler
import io.vertx.groovy.ext.web.handler.StaticHandler

def router = Router.router(vertx)

// Create a JWT Auth Provider
def jwt = JWTAuth.create([
  keyStoreType:"jceks",
  keyStoreURI:"classpath:///keystore.jceks",
  keyStorePassword:"secret"
])

// this route is excluded from the auth handler (it represents your login endpoint)
router.get("/api/newToken").handler({ ctx ->
  def authorities = ctx.request().params().getAll("authority")

  ctx.response().putHeader("Content-Type", "text/plain")

  if (authorities != null) {
    ctx.response().end(jwt.generateToken([:], [
      expiresInSeconds:60,
      permissions:authorities
    ]))
  } else {
    ctx.response().end(jwt.generateToken([:], [
      expiresInSeconds:60
    ]))
  }
})

router.route("/api/protected*").handler(JWTAuthHandler.create(jwt))

router.get("/api/protected").handler({ ctx ->
  // protected the API (any authority is allowed)
  ctx.response().putHeader("Content-Type", "text/plain")
  ctx.response().end("this secret is not defcon!")
})

router.get("/api/protected/defcon1").handler({ ctx ->
  // protect the API (defcon1 authority is required)
  ctx.user().isAuthorised("defcon1", { allowed ->
    if (allowed.failed()) {
      ctx.fail(allowed.cause())
      return
    }

    // user does not have the required authority
    if (!allowed.result()) {
      ctx.response().setStatusCode(403).end()
      return
    }

    ctx.response().putHeader("Content-Type", "text/plain")
    ctx.response().end("this secret is defcon1!")
  })
})

router.get("/api/protected/defcon2").handler({ ctx ->
  // protect the API (defcon2 authority is required)
  ctx.user().isAuthorised("defcon2", { allowed ->
    if (allowed.failed()) {
      ctx.fail(allowed.cause())
      return
    }

    // user does not have the required authority
    if (!allowed.result()) {
      ctx.response().setStatusCode(403).end()
      return
    }

    ctx.response().putHeader("Content-Type", "text/plain")
    ctx.response().end("this secret is defcon2!")
  })
})

router.get("/api/protected/defcon3").handler({ ctx ->
  // protect the API (defcon3 authority is required)
  ctx.user().isAuthorised("defcon3", { allowed ->
    if (allowed.failed()) {
      ctx.fail(allowed.cause())
      return
    }

    // user does not have the required authority
    if (!allowed.result()) {
      ctx.response().setStatusCode(403).end()
      return
    }

    ctx.response().putHeader("Content-Type", "text/plain")
    ctx.response().end("this secret is defcon3!")
  })
})

// Serve the non private static pages
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
