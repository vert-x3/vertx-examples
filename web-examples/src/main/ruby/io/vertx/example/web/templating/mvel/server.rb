require 'vertx-web/router'
require 'vertx-web-templ-mvel/mvel_template_engine'
require 'vertx-web/template_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# Serve the dynamic pages
router.route("/dynamic/*").handler(&VertxWeb::TemplateHandler.create(VertxWebTemplMvel::MVELTemplateEngine.create()).method(:handle))

# Serve the static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
