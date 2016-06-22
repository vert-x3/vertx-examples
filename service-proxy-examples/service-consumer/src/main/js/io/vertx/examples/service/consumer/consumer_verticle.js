var ProcessorService = require("vertx-processor-sample-js/processor_service");
var service = ProcessorService.createProxy(vertx, "vertx.processor");

var document = {
  "name" : "vertx"
};

service.process(document, function (r, r_err) {
  if (r_err == null) {
    console.log(JSON.stringify(r));
  } else {
    if (r_err.getClass().getSimpleName() == 'ServiceException') {
      var exc = r_err;
      if (exc.failureCode() === BAD_NAME_ERROR) {
        console.log("Failed to process the document: The name in the document is bad. The name provided is: " + exc.getDebugInfo().name);
      } else if (exc.failureCode() === NO_NAME_ERROR) {
        console.log("Failed to process the document: No name was found");
      }
    } else {
      console.log("Unexpected error: " + r_err);

    }
  }
});
