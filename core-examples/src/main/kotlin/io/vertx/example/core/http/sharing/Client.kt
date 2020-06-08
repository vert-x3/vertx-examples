package io.vertx.example.core.http.sharing

import io.vertx.core.http.HttpClientResponse

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.setPeriodic(1000, { l ->
      vertx.createHttpClient().get(8080, "localhost", "/").compose<Any>({ HttpClientResponse.body() }).onSuccess({ body ->
        println(body.toString("ISO-8859-1"))
      }).onFailure({ err ->
        err.printStackTrace()
      })
    })
  }
}
