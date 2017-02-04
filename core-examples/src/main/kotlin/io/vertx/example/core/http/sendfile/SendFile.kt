package io.vertx.example.core.http.sendfile

import io.vertx.kotlin.common.json.*

class SendFile : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // In reality it's highly recommend you use Vert.x-Web for applications like this.

    vertx.createHttpServer().requestHandler({ req ->
      var filename: String? = null
      if (req.path() == "/") {
        filename = "index.html"
      } else if (req.path() == "/page1.html") {
        filename = "page1.html"
      } else if (req.path() == "/page2.html") {
        filename = "page2.html"
      } else {
        req.response().setStatusCode(404).end()
      }
      if (filename != null) {
        req.response().sendFile(filename)
      }
    }).listen(8080)
  }
}
