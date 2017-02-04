package io.vertx.example.core.http.upload

import io.vertx.core.streams.Pump
import io.vertx.kotlin.common.json.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var req = vertx.createHttpClient(io.vertx.core.http.HttpClientOptions(
    )).put(8080, "localhost", "/someurl", { resp ->
      println("Response ${resp.statusCode()}")
    })
    var filename = "upload.txt"
    var fs = vertx.fileSystem()

    fs.props(filename, { ares ->
      var props = ares.result()
      println("props is ${props}")
      var size = props.size()
      req.headers().set("content-length", String.valueOf(size))
      fs.open(filename, io.vertx.core.file.OpenOptions(
      ), { ares2 ->
        var file = ares2.result()
        var pump = Pump.pump(file, req)
        file.endHandler({ v ->
          req.end()
        })
        pump.start()
      })
    })


  }
}
