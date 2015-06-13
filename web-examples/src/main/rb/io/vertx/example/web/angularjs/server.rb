require 'json'
require 'vertx-mongo/mongo_client'
require 'vertx-web/router'
require 'vertx-web/body_handler'
require 'vertx-web/static_handler'
@mongo
def load_data(db)
  db.drop_collection("users") { |drop,drop_err|
    if (drop_err != nil)
      raise drop_err
    end

    users = Java::JavaUtil::LinkedList.new()

    users.push({
      'username' => "pmlopes",
      'firstName' => "Paulo",
      'lastName' => "Lopes",
      'address' => "The Netherlands"
    })

    users.push({
      'username' => "timfox",
      'firstName' => "Tim",
      'lastName' => "Fox",
      'address' => "The Moon"
    })

    users.each do |user|
      db.insert("users", user) { |res,res_err|
        puts "inserted #{JSON.generate(user)}"
      }
    end
  }
end

# Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
@mongo = VertxMongo::MongoClient.create_shared($vertx, {
  'db_name' => "demo"
})

# the load function just populates some data on the storage
load_data(@mongo)

router = VertxWeb::Router.router($vertx)

router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))

# define some REST API
router.get("/api/users").handler() { |ctx|
  @mongo.find("users", {
  }) { |lookup,lookup_err|
    # error handling
    if (lookup_err != nil)
      ctx.fail(500)
      return
    end

    # now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    json = [
    ]

    lookup.each do |o|
      json.push(o)
    end

    ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "application/json")
    ctx.response().end(JSON.generate(json))
  }
}

router.get("/api/users/:id").handler() { |ctx|
  @mongo.find_one("users", {
    'id' => ctx.request().get_param("id")
  }, nil) { |lookup,lookup_err|
    # error handling
    if (lookup_err != nil)
      ctx.fail(500)
      return
    end

    user = lookup

    if (user == nil)
      ctx.fail(404)
    else
      ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "application/json")
      ctx.response().end(JSON.generate(user))
    end
  }
}

router.post("/api/users").handler() { |ctx|
  newUser = ctx.get_body_as_json()

  @mongo.find_one("users", {
    'username' => newUser['username']
  }, nil) { |lookup,lookup_err|
    # error handling
    if (lookup_err != nil)
      ctx.fail(500)
      return
    end

    user = lookup

    if (user != nil)
      # already exists
      ctx.fail(500)
    else
      @mongo.insert("users", newUser) { |insert,insert_err|
        # error handling
        if (insert_err != nil)
          ctx.fail(500)
          return
        end

        # add the generated id to the user object
        newUser['id'] = insert

        ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "application/json")
        ctx.response().end(JSON.generate(newUser))
      }
    end
  }
}

router.put("/api/users/:id").handler() { |ctx|
  @mongo.find_one("users", {
    'id' => ctx.request().get_param("id")
  }, nil) { |lookup,lookup_err|
    # error handling
    if (lookup_err != nil)
      ctx.fail(500)
      return
    end

    user = lookup

    if (user == nil)
      # does not exist
      ctx.fail(404)
    else

      # update the user properties
      update = ctx.get_body_as_json()

      user['username'] = update['username']
      user['firstName'] = update['firstName']
      user['lastName'] = update['lastName']
      user['address'] = update['address']

      @mongo.replace("users", {
        'id' => ctx.request().get_param("id")
      }, user) { |replace,replace_err|
        # error handling
        if (replace_err != nil)
          ctx.fail(500)
          return
        end

        ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "application/json")
        ctx.response().end(JSON.generate(user))
      }
    end
  }
}

router.delete("/api/users/:id").handler() { |ctx|
  @mongo.find_one("users", {
    'id' => ctx.request().get_param("id")
  }, nil) { |lookup,lookup_err|
    # error handling
    if (lookup_err != nil)
      ctx.fail(500)
      return
    end

    user = lookup

    if (user == nil)
      # does not exist
      ctx.fail(404)
    else

      @mongo.remove("users", {
        'id' => ctx.request().get_param("id")
      }) { |remove,remove_err|
        # error handling
        if (remove_err != nil)
          ctx.fail(500)
          return
        end

        ctx.response().set_status_code(204)
        ctx.response().end()
      }
    end
  }
}

# Create a router endpoint for the static content.
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
