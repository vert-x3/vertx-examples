var MongoClient = require("vertx-mongo-js/mongo_client");

var config = Java.type("io.vertx.groovy.core.Vertx").currentContext().config()
var uri = config.getString("mongo.uri", "mongodb://localhost:27017")
var db = config.getString("mongo.db", "test")

client = MongoClient.createShared(vertx, { "connection_string": uri, "db_name": db})

product = {"itemId":"12345", "name": "Cooler", "price":100.0}

client.save("products", product, function(id) {
  console.log("Inserted id: " + id.result())

  client.find("products", {"itemId":"12345"},  function(res) {
    console.log(println "Name is " + res.result().get(0).getString("name"))

    client.remove("products", {"itemId":"12345"}, function(rs) {
      if (rs.succeeded()){
        console.log("Product removed ")
      }
    })
  })

})