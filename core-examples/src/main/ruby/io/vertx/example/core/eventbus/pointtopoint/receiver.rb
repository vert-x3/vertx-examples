
eb = $vertx.event_bus()

eb.consumer("ping-address") { |message|

  puts "Received message: #{message.body()}"
  # Now send back reply
  message.reply("pong!")
}

puts "Receiver ready!"
