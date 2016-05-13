import io.vertx.core.http.HttpVersion
import io.vertx.core.net.SSLEngine

// Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

def options = [
  ssl:true,
  useAlpn:true,
  sslEngine:SSLEngine.OPENSSL,
  protocolVersion:HttpVersion.HTTP_2,
  trustAll:true
]

vertx.createHttpClient(options).getNow(8443, "localhost", "/", { resp ->
  println("Got response ${resp.statusCode()}")
  resp.bodyHandler({ body ->
    println("Got data ${body.toString("ISO-8859-1")}")
  })
})
