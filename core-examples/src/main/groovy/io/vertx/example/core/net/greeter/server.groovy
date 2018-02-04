import io.vertx.core.parsetools.RecordParser

vertx.createNetServer().connectHandler({ sock ->

  def parser = RecordParser.newDelimited("\n", sock)

  parser.endHandler({ v ->
    sock.close()
  }).exceptionHandler({ t ->
    t.printStackTrace()
    sock.close()
  }).handler({ buffer ->
    def name = buffer.toString("UTF-8")
    sock.write("Hello ${name}\n", "UTF-8")
  })

}).listen(1234)

println("Echo server is now listening")

