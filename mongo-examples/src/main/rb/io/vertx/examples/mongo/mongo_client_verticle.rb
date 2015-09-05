require 'vertx-mongo/mongo_client'

config = Java::IoVertxGroovyCore::Vertx.current_context().config()

uri = config.getString("mongo.uri", "mongodb://localhost:27017")
db = config.getString("mongo.db", System.getProperty("mongo.db", "test"))


# Create the redis client
client = VertxMongo::MongoClient.create($vertx, {
  'connection_string' => uri,
  'db_name' => db
})


product = {"itemId" => "12345", "name" => "Cooler", "price" => 100.0}


client.save("products", product) { |err, r|
  puts "Inserted id: #{id.result()}"

  client.find("products", {"itemId" => "12345"}) { | f_err, f_r|
    puts "Name is #{f_r.result().get(0).getString('name')}"

    client.remove("products", {"itemId":"12345"}) { | d_err |
      if (r_err == nil)
        puts "Product removed "
      end
     }
  }
}