
var eb = vertx.eventBus();

eb.consumer("news-feed", function (message) {
  console.log("Received news on consumer 1: " + message.body());
});

eb.consumer("news-feed", function (message) {
  console.log("Received news on consumer 2: " + message.body());
});

eb.consumer("news-feed", function (message) {
  console.log("Received news on consumer 3: " + message.body());
});

console.log("Ready!");
