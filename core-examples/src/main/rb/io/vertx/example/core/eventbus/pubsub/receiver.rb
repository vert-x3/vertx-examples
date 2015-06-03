
eb = $vertx.event_bus()

eb.consumer("news-feed") { |message|
  puts "Received news: #{message.body()}";
}

puts "Ready!"
