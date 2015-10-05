var EventBus = require('vertx3-eventbus-client');

var eb = new EventBus('http://localhost:8080/eventbus/');

eb.onopen = function () {
  eb.registerHandler('feed', function (err, msg) {
    console.log(msg.body.now);
  });
};
