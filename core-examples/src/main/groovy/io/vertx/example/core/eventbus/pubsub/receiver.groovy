
def eb = vertx.eventBus()

eb.consumer("news-feed", { message ->
  println("Received news: ${message.body()}")})

println("Ready!")
