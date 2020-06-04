import io.vertx.ext.web.Router
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.ext.auth.jwt.authorization.JWTAuthorization
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.web.handler.StaticHandler

def router = Router.router(vertx)

// Create a JWT Auth Provider
def jwt = JWTAuth.create(vertx, [
  keyStore:[
    type:"jceks",
    path:"keystore.jceks",
    password:"secret"
  ]
])

// this route is excluded from the auth handler (it represents your login endpoint)
router.get("/api/newToken").handler({ ctx ->
  def authorities = []

  ctx.request().params().getAll("authority").each { authority ->
    authorities.add(authority)
  }

  ctx.response().putHeader("Content-Type", "text/plain")
  ctx.response().end(jwt.generateToken([:], [
    expiresInSeconds:60,
    permissions:authorities
  ]))
})

router.route("/api/protected*").handler(JWTAuthHandler.create(jwt))

def authzProvider = JWTAuthorization.create("permissions")

router.get("/api/protected").handler({ ctx ->
  // protected the API (any authority is allowed)
  ctx.response().putHeader("Content-Type", "text/plain")
  ctx.response().end("this secret is not defcon!")
})

def defcon1 = PermissionBasedAuthorization.create("defcon1")
router.get("/api/protected/defcon1").handler({ ctx ->
  def user = ctx.user()
  authzProvider.getAuthorizations(user).onComplete({ ar ->
    if (ar.succeeded()) {
      // protect the API (defcon1 authority is required)
      if (defcon1.match(user)) {
        ctx.response().putHeader("Content-Type", "text/plain")
        ctx.response().end("this secret is defcon1!")
      } else {
        ctx.response().setStatusCode(403).end()
      }
    } else {
      ctx.fail(ar.cause())
    }
  })
})

def defcon2 = PermissionBasedAuthorization.create("defcon2")
router.get("/api/protected/defcon2").handler({ ctx ->
  def user = ctx.user()
  authzProvider.getAuthorizations(user).onComplete({ ar ->
    if (ar.succeeded()) {
      // protect the API (defcon2 authority is required)
      if (defcon2.match(user)) {
        ctx.response().putHeader("Content-Type", "text/plain")
        ctx.response().end("this secret is defcon2!")
      } else {
        ctx.response().setStatusCode(403).end()
      }
    } else {
      ctx.fail(ar.cause())
    }
  })
})

def defcon3 = PermissionBasedAuthorization.create("defcon3")
router.get("/api/protected/defcon3").handler({ ctx ->
  def user = ctx.user()
  authzProvider.getAuthorizations(user).onComplete({ ar ->
    if (ar.succeeded()) {
      // protect the API (defcon3 authority is required)
      if (defcon3.match(user)) {
        ctx.response().putHeader("Content-Type", "text/plain")
        ctx.response().end("this secret is defcon3!")
      } else {
        ctx.response().setStatusCode(403).end()
      }
    } else {
      ctx.fail(ar.cause())
    }
  })
})

// Serve the non private static pages
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router).listen(8080)
