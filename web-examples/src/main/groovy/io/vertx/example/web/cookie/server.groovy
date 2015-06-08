import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.CookieHandler
import io.vertx.groovy.ext.web.Cookie
import io.vertx.groovy.ext.web.handler.StaticHandler

def router = Router.router(vertx)

// This cookie handler will be called for all routes
router.route().handler(CookieHandler.create())

// on every path increment the counter
router.route().handler({ ctx ->
  def someCookie = ctx.getCookie("visits")

  def visits = 0
  if (someCookie != null) {
    def cookieValue = someCookie.getValue()
    try {
      visits = java.lang.Long.parseLong(cookieValue)
    } catch(Exception e) {
      visits = 0L
    }

  }

  // increment the tracking
  visits++

  // Add a cookie - this will get written back in the response automatically
  ctx.addCookie(Cookie.cookie("visits", "${visits}"))

  ctx.next()
})

// Serve the static resources
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
