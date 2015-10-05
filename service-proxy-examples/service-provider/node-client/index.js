var EventBus = require('vertx3-eventbus-client');
var ProcessorService = require('../src/main/resources/vertx-processor-sample-js/processor_service-proxy');

var eb = new EventBus('http://localhost:8080/eventbus/');

eb.onopen = function () {
  var processorService = new ProcessorService(eb, "vertx.processor");

  processorService.process({foo: 'bar'}, function (err, res) {
    if (err) {
      console.error(err);
      return;
    }

    console.log(res);
  });
};
