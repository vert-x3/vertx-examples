import io.vertx.groovy.core.streams.Pump
vertx.createHttpServer().requestHandler({ req ->
  req.pause()
  def filename = "${java.util.UUID.randomUUID()}.uploaded"
  vertx.fileSystem().open(filename, [:], { ares ->
    def file = ares.result()
    def pump = Pump.pump(req, file)
    req.endHandler({ v1 ->
      file.close({ v2 ->
        println("Uploaded to ${filename}")
        req.response().end()
      });
    })
    pump.start()
    req.resume()
  })
}).listen(8080)
