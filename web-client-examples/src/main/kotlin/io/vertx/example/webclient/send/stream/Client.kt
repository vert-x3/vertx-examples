package io.vertx.example.webclient.send.stream

import io.vertx.core.file.OpenOptions
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.core.file.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var filename = "upload.txt"
    var fs = vertx.fileSystem()

    var client = WebClient.create(vertx)

    fs.props(filename, { ares ->
      var props = ares.result()
      println("props is ${props}")
      var size = props.size()

      var req = client.put(8080, "localhost", "/")
      req.putHeader("content-length", "${size}")

      fs.open(filename, OpenOptions(), { ares2 ->
        req.sendStream(ares2.result(), { ar ->
          if (ar.succeeded()) {
            var response = ar.result()
            println("Got HTTP response with status ${response.statusCode()}")
          } else {
            ar.cause().printStackTrace()
          }
        })
      })
    })
  }
}
