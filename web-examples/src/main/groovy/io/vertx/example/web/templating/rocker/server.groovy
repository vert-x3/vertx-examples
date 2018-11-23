import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.rocker.RockerTemplateEngine
import io.vertx.ext.web.handler.TemplateHandler

def router = Router.router(vertx)

// Populate context with data
router.route().handler({ ctx ->
  ctx.put("title", "Vert.x Web Example Using Rocker")
  ctx.put("name", "Rocker")
  ctx.next()
})

// Render a custom template.
// Note: you need a compile-time generator for Rocker to work properly
// See the pom.xml for an example
router.route().handler(TemplateHandler.create(RockerTemplateEngine.create()))

vertx.createHttpServer().requestHandler(router).listen(8080)
