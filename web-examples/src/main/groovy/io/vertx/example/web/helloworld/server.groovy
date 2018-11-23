import io.vertx.ext.web.Router

def router = Router.router(vertx)

router.route().handler({ routingContext ->
  routingContext.response().putHeader("content-type", "text/html").end("Hello World!")
})

vertx.createHttpServer().requestHandler(router).listen(8080)
