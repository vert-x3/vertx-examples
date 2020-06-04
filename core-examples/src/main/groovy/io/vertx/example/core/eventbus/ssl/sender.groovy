def eb = vertx.eventBus()

// Send a message every second

vertx.setPeriodic(1000, { v ->

  eb.request("ping-address", "ping!", { ar ->
    if (ar.succeeded()) {
      println("Received reply ${ar.result().body()}")
    } else {
      println("No reply")
    }
  })

})
