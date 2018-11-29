require 'vertx-web/router'
require 'vertx-web-templ-rocker/rocker_template_engine'
require 'vertx-web/template_handler'

router = VertxWeb::Router.router($vertx)

# Populate context with data
router.route().handler() { |ctx|
  ctx.put("title", "Vert.x Web Example Using Rocker")
  ctx.put("name", "Rocker")
  ctx.next()
}

# Render a custom template.
# Note: you need a compile-time generator for Rocker to work properly
# See the pom.xml for an example
router.route().handler(&VertxWeb::TemplateHandler.create(VertxWebTemplRocker::RockerTemplateEngine.create()).method(:handle))

$vertx.create_http_server().request_handler(&router.method(:handle)).listen(8080)
