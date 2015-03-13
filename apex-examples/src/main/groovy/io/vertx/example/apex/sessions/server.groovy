import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.CookieHandler
import io.vertx.groovy.ext.apex.handler.SessionHandler
import io.vertx.groovy.ext.apex.sstore.LocalSessionStore

def router = Router.router(vertx)

router.route().handler(CookieHandler.create())
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

router.route().handler({ routingContext ->

  def session = routingContext.session()

  def cnt = session.get("hitcount")
  cnt = (cnt == null ? 0 : cnt) + 1

  session.put("hitcount", cnt)

  routingContext.response().putHeader("content-type", "text/html").end("<html><body><h1>Hitcount: ${cnt}</h1></body></html>")
})

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
