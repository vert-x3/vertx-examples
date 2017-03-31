package io.vertx.example.core.net.echo

import io.vertx.core.streams.Pump

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    vertx.createNetServer().connectHandler({ sock ->

      // Create a pump
      Pump.pump(sock, sock).start()

    }).listen(1234)

    println("Echo server is now listening")

  }
}
