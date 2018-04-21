import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.RockerTemplateEngine
import io.vertx.ext.web.handler.TemplateHandler

// Note: you need a compile-time generator for Rocker to work properly
// See the pom.xml for an example
def router = Router.router(vertx)

// Populate context with data
router.route().handler({ ctx ->
  ctx.put("title", "Vert.x Web Example Using Rocker")
  ctx.put("name", "Rocker")
  ctx.next()
})

// Render a custom template.
router.route().handler(TemplateHandler.create(RockerTemplateEngine.create()))

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
