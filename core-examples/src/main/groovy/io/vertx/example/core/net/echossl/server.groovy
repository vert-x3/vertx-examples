import io.vertx.groovy.core.streams.Pump

def options = [
  ssl:true,
  keyStoreOptions:[
    path:"server-keystore.jks",
    password:"wibble"
  ]
]

vertx.createNetServer(options).connectHandler({ sock ->

  // Create a pump
  Pump.pump(sock, sock).start()

}).listen(1234)

println("Echo server is now listening")
