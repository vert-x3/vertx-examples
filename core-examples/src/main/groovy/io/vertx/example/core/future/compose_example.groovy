import io.vertx.core.Future
def anotherAsyncAction(name) {
  def future = Future.future()
  // mimic something that take times
  vertx.setTimer(100, { l ->
    future.complete("hello ${name}")
  })
  return future
}
def anAsyncAction() {
  def future = Future.future()
  // mimic something that take times
  vertx.setTimer(100, { l ->
    future.complete("world")
  })
  return future
}
def future = this.anAsyncAction()
future.compose(this.&anotherAsyncAction).setHandler({ ar ->
  if (ar.failed()) {
    println("Something bad happened")
    ar.cause().printStackTrace()
  } else {
    println("Result: ${ar.result()}")
  }
})
