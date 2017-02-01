import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createNetClient().connect(1234, "localhost", { res ->

      if (res.succeeded()) {
        var socket = res.result()
        socket.handler({ buffer ->
          println("Net client receiving: ${buffer.toString("UTF-8")}")
        })

        // Now send some data
        for (i in 0 until 10) {
          var str = "hello ${i}\n"
          println("Net client sending: ${str}")
          socket.write(str)

        }

      } else {
        println("Failed to connect ${res.cause()}")
      }
    })
  }
}
