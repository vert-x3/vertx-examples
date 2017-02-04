package io.vertx.example.core.net.echossl

import io.vertx.core.streams.Pump
import io.vertx.kotlin.common.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = io.vertx.core.net.NetServerOptions(
      ssl = true,
      keyStoreOptions = io.vertx.core.net.JksOptions(
        path = "server-keystore.jks",
        password = "wibble"))

    vertx.createNetServer(options).connectHandler({ sock ->

      // Create a pump
      Pump.pump(sock, sock).start()

    }).listen(1234)

    println("Echo server is now listening")
  }
}
