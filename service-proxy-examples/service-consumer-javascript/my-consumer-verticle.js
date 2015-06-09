var vertx = require("vertx-js/vertx");
var ProcessorService = require("processor_service.js");

if (! ProcessorService  || ProcessorService === undefined) {
    console.log("Oh no, ProcessorService is undefined");
}

var processor = ProcessorService.createProxy(vertx, "vertx.processor");

//console.log(typeof ProcessorService);

//ProcessorService.process(null, null);

//var processor = ProcessorService.createProxy(vertx, "vertx.processor");

console.log("processor " + processor);

