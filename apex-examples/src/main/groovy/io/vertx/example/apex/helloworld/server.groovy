import io.vertx.groovy.ext.apex.Router

def router = Router.router(vertx)

router.route().handler({ routingContext ->
  routingContext.response().putHeader("content-type", "text/html").end("Hello World!")
})

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
