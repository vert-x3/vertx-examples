import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler

def router = Router.router(vertx)

// Serve the static pages
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router).listen(8080)

println("Server is started")

