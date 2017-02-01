import io.vertx.core.streams.Pump
import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    vertx.createHttpServer().requestHandler({ req ->
      req.pause()
      var filename = "${java.util.UUID.randomUUID()}.uploaded"
      vertx.fileSystem().open(filename, io.vertx.core.file.OpenOptions(
      ), { ares ->
        var file = ares.result()
        var pump = Pump.pump(req, file)
        req.endHandler({ v1 ->
          file.close({ v2 ->
            println("Uploaded to ${filename}")
            req.response().end()
          })
        })
        pump.start()
        req.resume()
      })
    }).listen(8080)
  }
}
