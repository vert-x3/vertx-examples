var MongoClient = require("vertx-mongo-js/mongo_client");

var config = Java.type("io.vertx.groovy.core.Vertx").currentContext().config
var uri = config["mongo.uri"] != null ? config["mongo.uri"] : "mongodb://localhost:27017"
var db = config["mongo.db"] != null ? config["mongo.db"] : "test"

var client = MongoClient.createShared(vertx, { "connection_string": uri, "db_name": db})

var product = {"itemId":"12345", "name": "Cooler", "price":"100.0"}

client.save("products", product, function(id, ins_err) {
  console.log("Inserted id: " + id)

  client.find("products", {"itemId":"12345"},  function(f_result, fin_err) {
    console.log("Name is " + f_result[0]["name"])

    client.remove("products", {"itemId":"12345"}, function(d_result, del_err) {
      if (!del_err){
        console.log("Product removed ")
      }
    })
  })

})