
def options = [
  ssl:true,
  trustAll:true
]

vertx.createNetClient(options).connect(1234, "localhost", { res ->
  if (res.succeeded()) {
    def sock = res.result()
    sock.handler({ buff ->
      println("client receiving ${buff.toString("UTF-8")}")
    })

    // Now send some data
    for (def i = 0;i < 10;i++) {
      def str = "hello ${i}
      "
      println("Net client sending: ${str}")
      sock.write(str)
    }
  } else {
    println("Failed to connect ${res.cause()}")
  }
})
