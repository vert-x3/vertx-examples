package io.vertx.example.webclient.send.multipartform

import io.vertx.core.MultiMap
import io.vertx.ext.web.client.WebClient

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

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

        var client = WebClient.create(vertx)

        var form = MultiMap.caseInsensitiveMultiMap()
        form.add("firstName", "Dale")
        form.add("lastName", "Cooper")
        form.add("male", "true")

        client.post(8080, "localhost", "/").putHeader("content-type", "multipart/form-data").sendForm(form, { ar ->
          if (ar.succeeded()) {
            var response = ar.result()
            println("Got HTTP response with status ${response.statusCode()}")
          } else {
            ar.cause().printStackTrace()
          }
        })
      }
    })
  }
}
