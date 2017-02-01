import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      if (req.uri() == "/") {
        // Serve the index page
        req.response().sendFile("index.html")
      } else if (req.uri().startsWith("/form")) {
        req.response().setChunked(true)
        req.setExpectMultipart(true)
        req.endHandler({ v ->
          for (attr in req.formAttributes().names()) {
            req.response().write("Got attr ${attr} : ${req.formAttributes().get(attr)}\n")
          }
          req.response().end()
        })
      } else {
        req.response().setStatusCode(404).end()
      }
    }).listen(8080)
  }
}
