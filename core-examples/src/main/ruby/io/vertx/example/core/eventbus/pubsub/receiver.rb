
eb = $vertx.event_bus()

eb.consumer("news-feed") { |message|
  puts "Received news on consumer 1: #{message.body()}"
}

eb.consumer("news-feed") { |message|
  puts "Received news on consumer 2: #{message.body()}"
}

eb.consumer("news-feed") { |message|
  puts "Received news on consumer 3: #{message.body()}"
}

puts "Ready!"
