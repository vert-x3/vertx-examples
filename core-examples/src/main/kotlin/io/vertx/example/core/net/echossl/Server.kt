package io.vertx.example.core.net.echossl

import io.vertx.core.net.JksOptions
import io.vertx.core.net.NetServerOptions
import io.vertx.core.streams.Pump
import io.vertx.kotlin.core.net.*

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var options = NetServerOptions(
      ssl = true,
      keyStoreOptions = JksOptions(
        path = "server-keystore.jks",
        password = "wibble"))

    vertx.createNetServer(options).connectHandler({ sock ->

      // Create a pump
      Pump.pump(sock, sock).start()

    }).listen(1234)

    println("Echo server is now listening")
  }
}
