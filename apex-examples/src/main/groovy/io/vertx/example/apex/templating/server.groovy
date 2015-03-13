import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.TemplateHandler
import io.vertx.groovy.ext.apex.templ.MVELTemplateEngine
import io.vertx.groovy.ext.apex.handler.StaticHandler

def router = Router.router(vertx)

// Serve the dynamic pages
router.route("/dynamic/*").handler(TemplateHandler.create(MVELTemplateEngine.create()))

// Serve the static pages
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
