package io.vertx.example.core.http2.customframes

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpVersion
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.core.http.*

class Client : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    var options = HttpClientOptions(
      ssl = true,
      useAlpn = true,
      protocolVersion = HttpVersion.HTTP_2,
      trustAll = true)

    var request = vertx.createHttpClient(options).get(8443, "localhost", "/")

    request.handler({ resp ->

      // Print custom frames received from server

      resp.customFrameHandler({ frame ->
        println("Got frame from server ${frame.payload().toString("UTF-8")}")
      })
    })

    request.sendHead({ version ->

      // Once head has been sent we can send custom frames

      vertx.setPeriodic(1000, { timerID ->

        println("Sending ping frame to server")
        request.writeCustomFrame(10, 0, Buffer.buffer("ping"))
      })
    })
  }
}
