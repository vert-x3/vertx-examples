package io.vertx.example.core.http.upload

import io.vertx.core.file.OpenOptions
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.streams.Pump
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.core.file.*
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var req = vertx.createHttpClient(HttpClientOptions()).put(8080, "localhost", "/someurl", { resp ->
      println("Response ${resp.statusCode()}")
    })
    var filename = "upload.txt"
    var fs = vertx.fileSystem()

    fs.props(filename, { ares ->
      var props = ares.result()
      println("props is ${props}")
      var size = props.size()
      req.headers().set("content-length", "${size}")
      fs.open(filename, OpenOptions(), { ares2 ->
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
