import io.vertx.core.Vertx
import io.vertx.ext.mongo.MongoClient

config = Vertx.currentContext().config()
uri = config.getString("mongo.uri", System.getProperty("mongo.uri", "mongodb://localhost:27017"))
db = config.getString("mongo.db", System.getProperty("mongo.db", "test"))

mongoClient = MongoClient.createShared(vertx, ["connection_string": uri, "db_name": db])

product = ["itemId":"12345", "name": "Cooler", "price":100.0]

mongoClient.save("products", product, { id ->
  println "Inserted id: ${id.result()}"

  mongoClient.find("products", ["itemId":"12345"],  { res ->
    println "Product Name is ${res.result()[0].getString("name")}"

    mongoClient.remove("products", ["itemId":"12345"], { rs ->
      if (rs.succeeded()){
        println "Product removed "
      }
    })

  })

})