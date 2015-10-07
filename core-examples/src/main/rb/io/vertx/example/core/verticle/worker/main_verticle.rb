puts "[Main] Running in #{Java::JavaLang::Thread.current_thread().get_name()}"
$vertx.deploy_verticle("io.vertx.example.core.verticle.worker.WorkerVerticle", {
  'worker' => true
})

$vertx.event_bus().send("sample.data", "hello vert.x") { |r_err,r|
  puts "[Main] Receiving reply ' #{r.body()}' in #{Java::JavaLang::Thread.current_thread().get_name()}"
}
