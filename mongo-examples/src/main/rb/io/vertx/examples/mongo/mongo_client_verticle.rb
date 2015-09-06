require 'vertx-mongo/mongo_client'

config = Java::IoVertxGroovyCore::Vertx.current_context().config()

uri = if config["mongo.uri"].nil? then "mongodb://localhost:27017" else config["mongo.uri"] end
db = if config["mongo.db"].nil? then "test" else config["mongo.db"] end

client = VertxMongo::MongoClient.create_shared($vertx, {
  'connection_string' => uri,
  'db_name' => db
})

product = {"itemId" => "12345", "name" => "Cooler", "price" => "100.0"}

client.save("products", product) { |err, id|
  puts "Inserted id: #{id}"

  client.find("products", {"itemId" => "12345"}) {|f_err, f_res|
    puts "Name is #{f_res[0]['name']}"

    client.remove("products", {"itemId" => "12345"}) {| d_err |
      if (d_err == nil)
        puts "Product removed"
      end
    }
  }
}