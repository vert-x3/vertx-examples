var ProcessorService = require("vertx-processor-sample-js/processor_service");
var service = ProcessorService.createProxy(vertx, "vertx.processor");

var document = {
  "name" : "vertx"
};

service.process(document, function (r, r_err) {
  if (r_err == null) {
    console.log(JSON.stringify(r));
  } else {
    Java.type("io.vertx.examples.service.consumer.Failures").dealWithFailure(r_err);
  }
});
