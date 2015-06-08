vertx.createHttpServer().requestHandler({ req ->
  def name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName()
  req.response().end("Happily served by ${name}")
}).listen(8080)
