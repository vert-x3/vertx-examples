var EventBus = require('@vertx/eventbus-bridge-client.js');
var ProcessorService = require('../src/main/java/io/vertx/examples/service/webroot/processor_service-proxy.js');

var eb = new EventBus('http://localhost:8080/eventbus/');

eb.onopen = function () {
  var processorService = new ProcessorService(eb, "vertx.processor");

  processorService.process({name: 'foo', value: 'bar'}, function (err, res) {
    if (err) {
      console.error(err);
      return;
    }

    console.log(res);
  });
};
