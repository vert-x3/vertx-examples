require 'vertx-web/router'
require 'vertx-web/cors_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

router.route().handler(&VertxWeb::CorsHandler.create("*").allowed_method(:GET).allowed_method(:POST).allowed_method(:OPTIONS).allowed_header("X-PINGARUNER").allowed_header("Content-Type").method(:handle))

router.get("/access-control-with-get").handler() { |ctx|

  ctx.response().set_chunked(true)

  ctx.request().headers().each do |entry|
    ctx.response().write(entry.get_key())
    ctx.response().write(entry.get_value())
    ctx.response().write("\n")
  end

  ctx.response().end()
}

router.post("/access-control-with-post-preflight").handler() { |ctx|
  ctx.response().set_chunked(true)

  ctx.request().headers().each do |entry|
    ctx.response().write(entry.get_key())
    ctx.response().write(entry.get_value())
    ctx.response().write("\n")
  end

  ctx.response().end()
}

# Serve the static resources
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
