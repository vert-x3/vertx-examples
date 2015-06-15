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
  authorities = ctx.request().params().get_all("authority")

  ctx.response().put_header("Content-Type", "text/plain")

  if (authorities != nil)
    ctx.response().end(jwt.generate_token({
    }, {
      'expiresInSeconds' => 60,
      'permissions' => authorities
    }))
  else
    ctx.response().end(jwt.generate_token({
    }, {
      'expiresInSeconds' => 60
    }))
  end
}

router.route("/api/protected*").handler(&VertxWeb::JWTAuthHandler.create(jwt).method(:handle))

router.get("/api/protected").handler() { |ctx|
  # protected the API (any authority is allowed)
  ctx.response().put_header("Content-Type", "text/plain")
  ctx.response().end("this secret is not defcon!")
}

router.get("/api/protected/defcon1").handler() { |ctx|
  # protect the API (defcon1 authority is required)
  ctx.user().is_authorised("defcon1") { |allowed_err,allowed|
    if (allowed_err != nil)
      ctx.fail(allowed_err)
      return
    end

    # user does not have the required authority
    if (!allowed)
      ctx.response().set_status_code(403).end()
      return
    end

    ctx.response().put_header("Content-Type", "text/plain")
    ctx.response().end("this secret is defcon1!")
  }
}

router.get("/api/protected/defcon2").handler() { |ctx|
  # protect the API (defcon2 authority is required)
  ctx.user().is_authorised("defcon2") { |allowed_err,allowed|
    if (allowed_err != nil)
      ctx.fail(allowed_err)
      return
    end

    # user does not have the required authority
    if (!allowed)
      ctx.response().set_status_code(403).end()
      return
    end

    ctx.response().put_header("Content-Type", "text/plain")
    ctx.response().end("this secret is defcon2!")
  }
}

router.get("/api/protected/defcon3").handler() { |ctx|
  # protect the API (defcon3 authority is required)
  ctx.user().is_authorised("defcon3") { |allowed_err,allowed|
    if (allowed_err != nil)
      ctx.fail(allowed_err)
      return
    end

    # user does not have the required authority
    if (!allowed)
      ctx.response().set_status_code(403).end()
      return
    end

    ctx.response().put_header("Content-Type", "text/plain")
    ctx.response().end("this secret is defcon3!")
  }
}

# Serve the non private static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
