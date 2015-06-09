puts "[Main] Running in #{Java::JavaLang::Thread.current_thread().get_name()}"
$vertx.deploy_verticle("io.vertx.example.core.verticle.worker.WorkerVerticle", {
  'worker' => true
})

$vertx.event_bus().send("sample.data", "hello vert.x") { |r,r_err|
  puts "[Main] Receiving reply ' #{r.body().to_string()}' in #{Java::JavaLang::Thread.current_thread().get_name()}"
}
