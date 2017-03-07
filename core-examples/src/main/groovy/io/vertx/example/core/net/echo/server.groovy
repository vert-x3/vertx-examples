import io.vertx.core.streams.Pump

vertx.createNetServer().connectHandler({ sock ->

  // Create a pump
  Pump.pump(sock, sock).start()

}).listen(1234)

println("Echo server is now listening")

