package io.vertx.example.core.http.https

import io.vertx.core.http.HttpClientOptions
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    var options = HttpClientOptions(
      ssl = true,
      trustAll = true)
    vertx.createHttpClient(options).get(4443, "localhost", "/").compose<Any>({ resp ->
      println("Got response ${resp.statusCode()}")
      return resp.body()
    }).onSuccess({ body ->
      println("Got data ${body.toString("ISO-8859-1")}")
    }).onFailure({ err ->
      err.printStackTrace()
    })
  }
}
