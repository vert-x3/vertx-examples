package io.vertx.example.core.http.simple


class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpClient().get(8080, "localhost", "/").compose<Any>({ resp ->
      println("Got response ${resp.statusCode()}")
      return resp.body()
    }).onSuccess({ body ->
      println("Got data ${body.toString("ISO-8859-1")}")
    }).onFailure({ err ->
      err.printStackTrace()
    })
  }
}
