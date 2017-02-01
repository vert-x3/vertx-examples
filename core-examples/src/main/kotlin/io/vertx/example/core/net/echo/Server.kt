import io.vertx.core.streams.Pump
import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    vertx.createNetServer().connectHandler({ sock ->

      // Create a pump
      Pump.pump(sock, sock).start()

    }).listen(1234)

    println("Echo server is now listening")

  }
}
