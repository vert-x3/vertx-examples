package io.vertx.example.web.sessions

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    router.route().handler(CookieHandler.create())
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

    router.route().handler({ routingContext ->

      var session = routingContext.session()

      var cnt = session.get<Any>("hitcount")
      cnt = (cnt == null ? 0 : cnt) + 1

      session.put("hitcount", cnt)

      routingContext.response().putHeader("content-type", "text/html").end("<html><body><h1>Hitcount: ${cnt}</h1></body></html>")
    })

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
