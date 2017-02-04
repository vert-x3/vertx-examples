require 'vertx-web/router'
require 'vertx-auth-jwt/jwt_auth'
require 'vertx-web/jwt_auth_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# Create a JWT Auth Provider
jwt = VertxAuthJwt::JWTAuth.create($vertx, {
  'keyStore' => {
    'type' => "jceks",
    'path' => "keystore.jceks",
    'password' => "secret"
  }
})

# this route is excluded from the auth handler (it represents your login endpoint)
router.get("/api/newToken").handler() { |ctx|

  authorities = Array.new

  ctx.request().params().get_all("authority").each do |authority|
    authorities.push(authority)
  end

  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end(jwt.generate_token({
  }, {
    'expiresInSeconds' => 60,
    'permissions' => authorities
  }))
}

# protect the API (any authority is allowed)
router.route("/api/protected").handler(&VertxWeb::JWTAuthHandler.create(jwt).method(:handle))

router.get("/api/protected").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end("this secret is not defcon!")
}

# protect the API (defcon1 authority is required)
router.route("/api/protected/defcon1").handler(&VertxWeb::JWTAuthHandler.create(jwt).add_authority("defcon1").method(:handle))

router.get("/api/protected/defcon1").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end("this secret is defcon1!")
}

# protect the API (defcon2 authority is required)
router.route("/api/protected/defcon2").handler(&VertxWeb::JWTAuthHandler.create(jwt).add_authority("defcon2").method(:handle))

router.get("/api/protected/defcon2").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end("this secret is defcon2!")
}

# protect the API (defcon3 authority is required)
router.route("/api/protected/defcon3").handler(&VertxWeb::JWTAuthHandler.create(jwt).add_authority("defcon3").method(:handle))

router.get("/api/protected/defcon3").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end("this secret is defcon3!")
}

# Serve the non private static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
