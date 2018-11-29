require 'vertx-web/router'

router = VertxWeb::Router.router($vertx)

router.route().handler() { |routingContext|
  routingContext.response().put_header("content-type", "text/html").end("Hello World!")
}

$vertx.create_http_server().request_handler(&router.method(:handle)).listen(8080)
