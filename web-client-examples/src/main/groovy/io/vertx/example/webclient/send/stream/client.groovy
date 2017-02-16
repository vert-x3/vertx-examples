import io.vertx.ext.web.client.WebClient

def filename = "upload.txt"
def fs = vertx.fileSystem()

def client = WebClient.create(vertx)

fs.props(filename, { ares ->
  def props = ares.result()
  println("props is ${props}")
  def size = props.size()

  def req = client.put(8080, "localhost", "/")
  req.putHeader("content-length", "${size}")

  fs.open(filename, [:], { ares2 ->
    req.sendStream(ares2.result(), { ar ->
      if (ar.succeeded()) {
        def response = ar.result()
        println("Got HTTP response with status ${response.statusCode()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  })
})
