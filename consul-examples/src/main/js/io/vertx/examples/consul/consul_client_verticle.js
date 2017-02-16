var ConsulClient = require("vertx-consul-js/consul_client");
var consulClient = ConsulClient.create(vertx);
consulClient.putValue("key11", "value11", function (putResult, putResult_err) {
  if (putResult_err == null) {
    console.log("KV pair saved");
    consulClient.getValue("key11", function (getResult, getResult_err) {
      if (getResult_err == null) {
        console.log("KV pair retrieved");
        console.log(getResult.value);
      } else {
        getResult_err.printStackTrace();
      }
    });
  } else {
    putResult_err.printStackTrace();
  }
});
