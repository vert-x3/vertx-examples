package io.vertx.example.core.http2.h2c

import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpVersion
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = HttpClientOptions(
      protocolVersion = HttpVersion.HTTP_2)

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
