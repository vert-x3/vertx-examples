require 'vertx-apex/router'

router = VertxApex::Router.router($vertx)

router.route().handler() { |routingContext|
  routingContext.response().put_header("content-type", "text/html").end("Hello World!")
}

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
