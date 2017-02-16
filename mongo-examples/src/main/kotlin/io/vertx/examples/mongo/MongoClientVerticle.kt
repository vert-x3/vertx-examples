package io.vertx.examples.mongo

import io.vertx.core.Vertx
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.*

class MongoClientVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var config = Vertx.currentContext().config()

    var uri = config.getString("mongo_uri")
    if (uri == null) {
      uri = "mongodb://localhost:27017"
    }
    var db = config.getString("mongo_db")
    if (db == null) {
      db = "test"
    }

    var mongoconfig = json {
      obj(
        "connection_string" to uri,
        "db_name" to db
      )
    }

    var mongoClient = MongoClient.createShared(vertx, mongoconfig)

    var product1 = json {
      obj(
        "itemId" to "12345",
        "name" to "Cooler",
        "price" to "100.0"
      )
    }

    mongoClient.save("products", product1, { id ->
      println("Inserted id: ${id.result()}")

      mongoClient.find("products", json {
        obj("itemId" to "12345")
      }, { res ->
        println("Name is ${res.result()[0].getString("name")}")

        mongoClient.remove("products", json {
          obj("itemId" to "12345")
        }, { rs ->
          if (rs.succeeded()) {
            println("Product removed ")
          }
        })

      })

    })

  }
}
