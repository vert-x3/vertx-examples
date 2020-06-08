package io.vertx.example.core.http2.simple

import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpVersion
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    var options = HttpClientOptions(
      ssl = true,
      useAlpn = true,
      protocolVersion = HttpVersion.HTTP_2,
      trustAll = true)

    vertx.createHttpClient(options).get(8080, "localhost", "/").compose<Any>({ resp ->
      println("Got response ${resp.statusCode()} with protocol ${resp.version()}")
      return resp.body()
    }).onSuccess({ body ->
      println("Got data ${body.toString("ISO-8859-1")}")
    }).onFailure({ err ->
      err.printStackTrace()
    })
  }
}
