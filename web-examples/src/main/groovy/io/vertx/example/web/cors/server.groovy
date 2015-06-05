import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.CorsHandler
import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.web.handler.StaticHandler

def router = Router.router(vertx)

router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.OPTIONS).allowedHeader("X-PINGARUNER").allowedHeader("Content-Type"))

router.get("/access-control-with-get").handler({ ctx ->

  ctx.response().setChunked(true)

  ctx.request().headers().each { entry ->
    ctx.response().write(entry.getKey())
    ctx.response().write(entry.getValue())
    ctx.response().write("\n")
  }

  ctx.response().end()
})

router.post("/access-control-with-post-preflight").handler({ ctx ->
  ctx.response().setChunked(true)

  ctx.request().headers().each { entry ->
    ctx.response().write(entry.getKey())
    ctx.response().write(entry.getValue())
    ctx.response().write("\n")
  }

  ctx.response().end()
})

// Serve the static resources
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
