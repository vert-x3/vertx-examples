import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.mongo.MongoClient

def config = Vertx.currentContext().config()

def uri = config.mongo_uri
if (uri == null) {
  uri = "mongodb://localhost:27017"
}
def db = config.mongo_db
if (db == null) {
  db = "test"
}

def mongoconfig = [
  connection_string:uri,
  db_name:db
]

def mongoClient = MongoClient.createShared(vertx, mongoconfig)

def product1 = [
  itemId:"12345",
  name:"Cooler",
  price:"100.0"
]

mongoClient.save("products", product1, { id ->
  println("Inserted id: ${id.result()}")

  mongoClient.find("products", [
    itemId:"12345"
  ], { res ->
    println("Name is ${res.result()[0].name}")

    mongoClient.remove("products", [
      itemId:"12345"
    ], { rs ->
      if (rs.succeeded()) {
        println("Product removed ")
      }
    })

  })

})

