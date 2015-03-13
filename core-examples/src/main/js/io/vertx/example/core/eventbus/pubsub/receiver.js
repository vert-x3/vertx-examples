
var eb = vertx.eventBus();

eb.consumer("news-feed", function (message) {
  console.log("Received news: " + message.body())});

console.log("Ready!");
