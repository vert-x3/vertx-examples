var WebClient = require("vertx-web-client-js/web_client");
var MultiMap = require("vertx-js/multi_map");

vertx.createHttpServer().requestHandler(function (req) {
  console.log("Got form with content-type " + req.getHeader("content-type"));
  req.setExpectMultipart(true);
  req.endHandler(function (v) {
    console.log("firstName: " + req.getFormAttribute("firstName"));
    console.log("lastName: " + req.getFormAttribute("lastName"));
    console.log("male: " + req.getFormAttribute("male"));
  });

}).listen(8080, function (listenResult, listenResult_err) {
  if (listenResult_err != null) {
    console.log("Could not start HTTP server");
    listenResult_err.printStackTrace();
  } else {

    var client = WebClient.create(vertx);

    var form = MultiMap.caseInsensitiveMultiMap();
    form.add("firstName", "Dale");
    form.add("lastName", "Cooper");
    form.add("male", "true");

    client.post(8080, "localhost", "/").putHeader("content-type", "multipart/form-data").sendForm(form, function (ar, ar_err) {
      if (ar_err == null) {
        var response = ar;
        console.log("Got HTTP response with status " + response.statusCode());
      } else {
        ar_err.printStackTrace();
      }
    });
  }
});
