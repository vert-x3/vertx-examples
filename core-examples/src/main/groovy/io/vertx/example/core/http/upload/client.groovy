import io.vertx.core.streams.Pump
def req = vertx.createHttpClient([:]).put(8080, "localhost", "/someurl", { resp ->
  println("Response ${resp.statusCode()}")
})
def filename = "upload.txt"
def fs = vertx.fileSystem()

fs.props(filename, { ares ->
  def props = ares.result()
  println("props is ${props}")
  def size = props.size()
  req.headers().set("content-length", java.lang.String.valueOf(size))
  fs.open(filename, [:], { ares2 ->
    def file = ares2.result()
    def pump = Pump.pump(file, req)
    file.endHandler({ v ->
      req.end()
    })
    pump.start()
  })
})


