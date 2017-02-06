package io.vertx.example.webclient.send.jsonobject

import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.core.json.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = WebClient.create(vertx)

    var user = json {
      obj(
        "firstName" to "Date",
        "lastName" to "Cooper",
        "male" to true
      )
    }

    client.put(8080, "localhost", "/").sendJson(user, { ar ->
      if (ar.succeeded()) {
        var response = ar.result()
        println("Got HTTP response with status ${response.statusCode()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
