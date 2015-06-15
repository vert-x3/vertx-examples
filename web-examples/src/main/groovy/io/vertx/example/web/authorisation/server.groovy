import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.auth.jwt.JWTAuth
import io.vertx.groovy.ext.web.handler.JWTAuthHandler
import io.vertx.groovy.ext.web.handler.StaticHandler

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

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
