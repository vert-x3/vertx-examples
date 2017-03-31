require 'vertx-web/router'

router = VertxWeb::Router.router($vertx)

router.route().blocking_handler(lambda { |routingContext|
  # Blocking handlers are allowed to block the calling thread
  # So let's simulate a blocking action or long running operation
  begin
    Java::JavaLang::Thread.sleep(5000)
  rescue
  end


  # Now call the next handler
  routingContext.next()
}, false)

router.route().handler() { |routingContext|
  routingContext.response().put_header("content-type", "text/html").end("Hello World!")
}

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
