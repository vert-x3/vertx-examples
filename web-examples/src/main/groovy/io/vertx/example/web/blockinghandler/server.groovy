import io.vertx.ext.web.Router

def router = Router.router(vertx)

router.route().blockingHandler({ routingContext ->
  // Blocking handlers are allowed to block the calling thread
  // So let's simulate a blocking action or long running operation
  try {
    java.lang.Thread.sleep(5000)
  } catch(Exception e) {
  }


  // Now call the next handler
  routingContext.next()
}, false)

router.route().handler({ routingContext ->
  routingContext.response().putHeader("content-type", "text/html").end("Hello World!")
})

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
