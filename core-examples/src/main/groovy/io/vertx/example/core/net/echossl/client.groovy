
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
    (0..<10).each { i ->
      def str = "hello ${i}\n"
      println("Net client sending: ${str}")
      sock.write(str)
    }
  } else {
    println("Failed to connect ${res.cause()}")
  }
})
