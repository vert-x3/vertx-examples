import io.vertx.core.Promise
def anotherAsyncAction(name) {
  def promise = Promise.promise()
  // mimic something that take times
  vertx.setTimer(100, { l ->
    promise.complete("hello ${name}")
  })
  return promise.future()
}
def anAsyncAction() {
  def promise = Promise.promise()
  // mimic something that take times
  vertx.setTimer(100, { l ->
    promise.complete("world")
  })
  return promise.future()
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
