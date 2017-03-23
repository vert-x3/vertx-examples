
def eb = vertx.eventBus()

eb.consumer("news-feed", { message ->
  println("Received news on consumer 1: ${message.body()}")
})

eb.consumer("news-feed", { message ->
  println("Received news on consumer 2: ${message.body()}")
})

eb.consumer("news-feed", { message ->
  println("Received news on consumer 3: ${message.body()}")
})

println("Ready!")
