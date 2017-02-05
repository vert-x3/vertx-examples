package io.vertx.example.core.http.proxy

import io.vertx.core.http.HttpClientOptions
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var request = vertx.createHttpClient(HttpClientOptions()).put(8080, "localhost", "/", { resp ->
      println("Got response ${resp.statusCode()}")
      resp.bodyHandler({ body ->
        println("Got data ${body.toString("ISO-8859-1")}")
      })
    })

    request.setChunked(true)

    for (i in 0 until 10) {
      request.write("client-chunk-${i}")

    }


    request.end()
  }
}
