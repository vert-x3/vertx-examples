import io.vertx.core.http.HttpVersion
import io.vertx.groovy.core.buffer.Buffer

// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

def options = [
  ssl:true,
  useAlpn:true,
  protocolVersion:"HTTP_2",
  trustAll:true
]

def request = vertx.createHttpClient(options).get(8443, "localhost", "/")

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
