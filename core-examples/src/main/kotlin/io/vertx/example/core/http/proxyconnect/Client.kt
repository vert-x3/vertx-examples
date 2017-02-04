package io.vertx.example.core.http.proxyconnect

import io.vertx.core.http.HttpClientOptions
import io.vertx.core.net.ProxyOptions
import io.vertx.core.net.ProxyType
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.core.http.*
import io.vertx.kotlin.core.net.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var request = vertx.createHttpClient(HttpClientOptions(
      proxyOptions = ProxyOptions(
        type = ProxyType.HTTP,
        host = "localhost",
        port = 8080))).put(8282, "localhost", "/", { resp ->
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
