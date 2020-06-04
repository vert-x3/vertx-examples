import io.vertx.ext.web.Router
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.web.handler.JWTAuthHandler
import io.vertx.ext.auth.jwt.authorization.JWTAuthorization
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization
import io.vertx.ext.web.handler.AuthorizationHandler
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

def authnHandler = JWTAuthHandler.create(jwt)
def authzProvider = JWTAuthorization.create("permissions")

// protect the API (any authority is allowed)
router.route("/api/*").handler(authnHandler)

router.get("/api/protected").handler({ ctx ->
  ctx.response().putHeader("Content-Type", "text/plain")
  ctx.response().end("this secret is not defcon!")
})

// protect the API (defcon1 authority is required)
def defcon1Handler = AuthorizationHandler.create(PermissionBasedAuthorization.create("defcon1")).addAuthorizationProvider(authzProvider)
router.route("/api/protected/defcon1").handler(defcon1Handler)

router.get("/api/protected/defcon1").handler({ ctx ->
  ctx.response().putHeader("Content-Type", "text/plain")
  ctx.response().end("this secret is defcon1!")
})

// protect the API (defcon2 authority is required)
def defcon2Handler = AuthorizationHandler.create(PermissionBasedAuthorization.create("defcon2")).addAuthorizationProvider(authzProvider)
router.route("/api/protected/defcon2").handler(defcon2Handler)

router.get("/api/protected/defcon2").handler({ ctx ->
  ctx.response().putHeader("Content-Type", "text/plain")
  ctx.response().end("this secret is defcon2!")
})

// protect the API (defcon3 authority is required)
def defcon3Handler = AuthorizationHandler.create(PermissionBasedAuthorization.create("defcon3")).addAuthorizationProvider(authzProvider)
router.route("/api/protected/defcon3").handler(defcon3Handler)

router.get("/api/protected/defcon3").handler({ ctx ->
  ctx.response().putHeader("Content-Type", "text/plain")
  ctx.response().end("this secret is defcon3!")
})

// Serve the non private static pages
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router).listen(8080)
