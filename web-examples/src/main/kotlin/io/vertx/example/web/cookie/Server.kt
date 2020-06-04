package io.vertx.example.web.cookie

import io.vertx.core.http.Cookie
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    // on every path increment the counter
    router.route().handler({ ctx ->
      var someCookie = ctx.getCookie("visits")

      var visits = 0
      if (someCookie != null) {
        var cookieValue = someCookie.getValue()
        try {
          visits = Long.parseLong(cookieValue)
        } catch(e: Exception) {
          visits = 0
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

    vertx.createHttpServer().requestHandler(router).listen(8080)
  }
}
