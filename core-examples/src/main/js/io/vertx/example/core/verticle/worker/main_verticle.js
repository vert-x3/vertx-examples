console.log("[Main] Running in " + Java.type("java.lang.Thread").currentThread().getName());
vertx.deployVerticle("io.vertx.example.core.verticle.worker.WorkerVerticle", {
  "worker" : true
});

vertx.eventBus().send("sample.data", "hello vert.x", function (r, r_err) {
  console.log("[Main] Receiving reply ' " + r.body() + "' in " + Java.type("java.lang.Thread").currentThread().getName());
});
