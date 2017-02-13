package io.vertx.example.webclient.send.multipartform

import io.vertx.core.MultiMap
import io.vertx.ext.web.client.WebClient

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var client = WebClient.create(vertx)

    var form = MultiMap.caseInsensitiveMultiMap()
    form.add("firstName", "Dale")
    form.add("lastName", "Cooper")
    form.add("male", "true")

    client.post(8080, "localhost", "/").putHeader("content-type", "multipart/form-data").sendForm(form, { ar ->
      if (ar.succeeded()) {
        var response = ar.result()
        println("Got HTTP response with status ${response.statusCode()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
