
vertx.setPeriodic(100, { l ->
  vertx.createHttpClient().getNow(8080, "localhost", "/", { resp ->
    resp.bodyHandler({ body ->
      println(body.toString("ISO-8859-1"))
    })
  })
})

