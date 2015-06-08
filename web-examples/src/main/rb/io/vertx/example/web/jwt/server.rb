require 'vertx-web/router'
require 'vertx-auth-jwt/jwt_auth'
require 'vertx-web/jwt_auth_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# Create a JWT Auth Provider
jwt = VertxAuthJwt::JWTAuth.create({
  'keyStoreType' => "jceks",
  'keyStoreURI' => "classpath:///keystore.jceks",
  'keyStorePassword' => "secret"
})

# protect the API
router.route("/api/*").handler(&VertxWeb::JWTAuthHandler.create(jwt, "/api/newToken").method(:handle))

# this route is excluded from the auth handler
router.get("/api/newToken").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end(jwt.generate_token({
  }, {
    'expiresInSeconds' => 60
  }))
}

# this is the secret API
router.get("/api/protected").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end("a secret you should keep for yourself...")
}

# Serve the non private static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
