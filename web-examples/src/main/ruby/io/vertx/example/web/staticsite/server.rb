require 'vertx-web/router'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# Serve the static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

puts "Server is started"

