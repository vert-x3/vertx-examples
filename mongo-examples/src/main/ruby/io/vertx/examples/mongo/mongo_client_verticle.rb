require 'vertx/vertx'
require 'vertx-mongo/mongo_client'

config = Vertx::Vertx.current_context().config()

uri = config['mongo_uri']
if (uri == nil)
  uri = "mongodb://localhost:27017"
end
db = config['mongo_db']
if (db == nil)
  db = "test"
end

mongoconfig = {
  'connection_string' => uri,
  'db_name' => db
}

mongoClient = VertxMongo::MongoClient.create_shared($vertx, mongoconfig)

product1 = {
  'itemId' => "12345",
  'name' => "Cooler",
  'price' => "100.0"
}

mongoClient.save("products", product1) { |id_err,id|
  puts "Inserted id: #{id}"

  mongoClient.find("products", {
    'itemId' => "12345"
  }) { |res_err,res|
    puts "Name is #{res[0]['name']}"

    mongoClient.remove("products", {
      'itemId' => "12345"
    }) { |rs_err,rs|
      if (rs_err == nil)
        puts "Product removed "
      end
    }

  }

}

