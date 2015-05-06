require 'vertx-apex/router'
require 'vertx-apex/cookie_handler'
require 'vertx-apex/session_handler'
require 'vertx-apex/local_session_store'

router = VertxApex::Router.router($vertx)

router.route().handler(&VertxApex::CookieHandler.create().method(:handle))
router.route().handler(&VertxApex::SessionHandler.create(VertxApex::LocalSessionStore.create($vertx)).method(:handle))

router.route().handler() { |routingContext|

  session = routingContext.session()

  cnt = session.get("hitcount")
  cnt = (cnt == nil ? 0 : cnt) + 1

  session.put("hitcount", cnt)

  routingContext.response().put_header("content-type", "text/html").end("<html><body><h1>Hitcount: #{cnt}</h1></body></html>")
}

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
