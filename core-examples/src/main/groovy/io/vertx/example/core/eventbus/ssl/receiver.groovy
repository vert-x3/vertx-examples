
def eb = vertx.eventBus()

eb.consumer("ping-address", { message ->

  println("Received message: ${message.body()}")
  // Now send back reply
  message.reply("pong!")
})

println("Receiver ready!")
