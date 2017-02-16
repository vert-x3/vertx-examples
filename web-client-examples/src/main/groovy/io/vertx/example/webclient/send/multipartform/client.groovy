import io.vertx.ext.web.client.WebClient
import io.vertx.core.MultiMap

vertx.createHttpServer().requestHandler({ req ->
  println("Got form with content-type ${req.getHeader("content-type")}")
  req.setExpectMultipart(true)
  req.endHandler({ v ->
    println("firstName: ${req.getFormAttribute("firstName")}")
    println("lastName: ${req.getFormAttribute("lastName")}")
    println("male: ${req.getFormAttribute("male")}")
  })

}).listen(8080, { listenResult ->
  if (listenResult.failed()) {
    println("Could not start HTTP server")
    listenResult.cause().printStackTrace()
  } else {

    def client = WebClient.create(vertx)

    def form = MultiMap.caseInsensitiveMultiMap()
    form.add("firstName", "Dale")
    form.add("lastName", "Cooper")
    form.add("male", "true")

    client.post(8080, "localhost", "/").putHeader("content-type", "multipart/form-data").sendForm(form, { ar ->
      if (ar.succeeded()) {
        def response = ar.result()
        println("Got HTTP response with status ${response.statusCode()}")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
})
