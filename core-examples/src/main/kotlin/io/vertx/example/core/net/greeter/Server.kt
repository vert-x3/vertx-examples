package io.vertx.example.core.net.greeter

import io.vertx.core.parsetools.RecordParser

class Server : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    vertx.createNetServer().connectHandler({ sock ->

      var parser = RecordParser.newDelimited("\n", sock)

      parser.endHandler({ v ->
        sock.close()
      }).exceptionHandler({ t ->
        t.printStackTrace()
        sock.close()
      }).handler({ buffer ->
        var name = buffer.toString("UTF-8")
        sock.write("Hello ${name}\n", "UTF-8")
      })

    }).listen(1234)

    println("Echo server is now listening")

  }
}
