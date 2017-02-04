var Vertx = require("vertx-js/vertx");
var MongoClient = require("vertx-mongo-js/mongo_client");

var config = Vertx.currentContext().config();

var uri = config.mongo_uri;
if (uri === null ||uri === undefined) {
  uri = "mongodb://localhost:27017";
}
var db = config.mongo_db;
if (db === null ||db === undefined) {
  db = "test";
}

var mongoconfig = {
  "connection_string" : uri,
  "db_name" : db
};

var mongoClient = MongoClient.createShared(vertx, mongoconfig);

var product1 = {
  "itemId" : "12345",
  "name" : "Cooler",
  "price" : "100.0"
};

mongoClient.save("products", product1, function (id, id_err) {
  console.log("Inserted id: " + id);

  mongoClient.find("products", {
    "itemId" : "12345"
  }, function (res, res_err) {
    console.log("Name is " + res[0].name);

    mongoClient.remove("products", {
      "itemId" : "12345"
    }, function (rs, rs_err) {
      if (rs_err == null) {
        console.log("Product removed ");
      }
    });

  });

});

