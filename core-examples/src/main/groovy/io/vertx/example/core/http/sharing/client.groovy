import io.vertx.core.http.HttpClientResponse
vertx.setPeriodic(1000, { l ->
  vertx.createHttpClient().get(8080, "localhost", "/").compose(HttpClientResponse.&body).onSuccess({ body ->
    println(body.toString("ISO-8859-1"))
  })
})
