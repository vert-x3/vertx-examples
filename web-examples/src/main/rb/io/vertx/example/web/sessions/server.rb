require 'vertx-web/router'
require 'vertx-web/cookie_handler'
require 'vertx-web/local_session_store'
require 'vertx-web/session_handler'

router = VertxWeb::Router.router($vertx)

router.route().handler(&VertxWeb::CookieHandler.create().method(:handle))
router.route().handler(&VertxWeb::SessionHandler.create(VertxWeb::LocalSessionStore.create($vertx)).method(:handle))

router.route().handler() { |routingContext|

  session = routingContext.session()

  cnt = session.get("hitcount")
  cnt = (cnt == nil ? 0 : cnt) + 1

  session.put("hitcount", cnt)

  routingContext.response().put_header("content-type", "text/html").end("<html><body><h1>Hitcount: #{cnt}</h1></body></html>")
}

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
