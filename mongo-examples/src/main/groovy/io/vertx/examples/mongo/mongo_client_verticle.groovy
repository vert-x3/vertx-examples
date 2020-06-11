import io.vertx.core.Vertx
import io.vertx.ext.mongo.MongoClient

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

mongoClient.save("products", product1).compose({ id ->
  println("Inserted id: ${id}")
  return mongoClient.find("products", [
    itemId:"12345"
  ])
}).compose({ res ->
  println("Name is ${res[0].name}")
  return mongoClient.removeDocument("products", [
    itemId:"12345"
  ])
}).onComplete({ ar ->
  if (ar.succeeded()) {
    println("Product removed ")
  } else {
    ar.cause().printStackTrace()
  }
})
