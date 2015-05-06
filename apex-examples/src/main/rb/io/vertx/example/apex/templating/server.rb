require 'vertx-apex/router'
require 'vertx-apex/template_handler'
require 'vertx-apex/mvel_template_engine'
require 'vertx-apex/static_handler'

router = VertxApex::Router.router($vertx)

# Serve the dynamic pages
router.route("/dynamic/*").handler(&VertxApex::TemplateHandler.create(VertxApex::MVELTemplateEngine.create()).method(:handle))

# Serve the static pages
router.route().handler(&VertxApex::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
