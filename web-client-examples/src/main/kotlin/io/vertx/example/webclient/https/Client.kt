package io.vertx.example.webclient.https

import io.vertx.core.net.JksOptions
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.kotlin.core.net.*
import io.vertx.kotlin.ext.web.client.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Create the web client and enable SSL/TLS with a trust store
    var client = WebClient.create(vertx, WebClientOptions(
      ssl = true,
      trustStoreOptions = JksOptions(
        path = "client-truststore.jks",
        password = "wibble")))

    client.get(8443, "localhost", "/").send({ ar ->
      if (ar.succeeded()) {
        var response = ar.result()
        println("Got HTTP response with status ${response.statusCode()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}
