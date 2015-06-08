vertx.createNetClient().connect(1234, "localhost", { res ->

  if (res.succeeded()) {
    def socket = res.result()
    socket.handler({ buffer ->
      println("Net client receiving: ${buffer.toString("UTF-8")}")
    })

    // Now send some data
    for (def i = 0;i < 10;i++) {
      def str = "hello ${i}
      "
      println("Net client sending: ${str}")
      socket.write(str)
    }
  } else {
    println("Failed to connect ${res.cause()}")
  }
})
