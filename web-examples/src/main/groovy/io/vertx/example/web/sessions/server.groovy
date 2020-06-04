import io.vertx.ext.web.Router
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.ext.web.handler.SessionHandler

def router = Router.router(vertx)

router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

router.route().handler({ routingContext ->

  def session = routingContext.session()

  def cnt = session.get("hitcount")
  cnt = (cnt == null ? 0 : cnt) + 1

  session.put("hitcount", cnt)

  routingContext.response().putHeader("content-type", "text/html").end("<html><body><h1>Hitcount: ${cnt}</h1></body></html>")
})

vertx.createHttpServer().requestHandler(router).listen(8080)
