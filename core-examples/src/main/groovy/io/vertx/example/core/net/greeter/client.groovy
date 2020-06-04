import io.vertx.core.parsetools.RecordParser
vertx.createNetClient().connect(1234, "localhost", { res ->

  if (res.succeeded()) {
    def socket = res.result()

    RecordParser.newDelimited("\n", socket).endHandler({ v ->
      socket.close()
    }).exceptionHandler({ t ->
      t.printStackTrace()
      socket.close()
    }).handler({ buffer ->
      def greeting = buffer.toString("UTF-8")
      println("Net client receiving: ${greeting}")
    })

    // Now send some data
    java.util.stream.Stream.of("John", "Joe", "Lisa", "Bill").forEach({ name ->
      println("Net client sending: ${name}")
      socket.write(name)
      socket.write("\n")
    })

  } else {
    println("Failed to connect ${res.cause()}")
  }
})
