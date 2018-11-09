package io.vertx.example.web.templating.mvel

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.TemplateHandler
import io.vertx.ext.web.templ.mvel.MVELTemplateEngine

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var router = Router.router(vertx)

    // Serve the dynamic pages
    router.route("/dynamic/*").handler({ ctx ->
      // put the context into the template render context
      ctx.put("context", ctx)
      ctx.next()
    }).handler(TemplateHandler.create(MVELTemplateEngine.create(vertx)))

    // Serve the static pages
    router.route().handler(StaticHandler.create())

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
