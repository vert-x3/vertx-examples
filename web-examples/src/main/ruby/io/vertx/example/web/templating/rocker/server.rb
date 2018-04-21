require 'vertx-web/router'
require 'vertx-web/rocker_template_engine'
require 'vertx-web/template_handler'

# Note: you need a compile-time generator for Rocker to work properly
# See the pom.xml for an example
router = VertxWeb::Router.router($vertx)

# Populate context with data
router.route().handler() { |ctx|
  ctx.put("title", "Vert.x Web Example Using Rocker")
  ctx.put("name", "Rocker")
  ctx.next()
}

# Render a custom template.
router.route().handler(&VertxWeb::TemplateHandler.create(VertxWeb::RockerTemplateEngine.create()).method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
