require 'json'
require 'vertx-mongo/mongo_client'
require 'vertx-web/router'
require 'vertx-web/cookie_handler'
require 'vertx-web/body_handler'
require 'vertx-web/local_session_store'
require 'vertx-web/session_handler'
require 'vertx-auth-shiro/shiro_auth'
require 'vertx-web/user_session_handler'
require 'vertx-web/sock_js_handler'
require 'vertx-web/static_handler'
@mongo
def list_albums(msg)
  # issue a find command to mongo to fetch all documents from the "albums" collection.
  @mongo.find("albums", {
  }) { |lookup_err,lookup|
    # error handling
    if (lookup_err != nil)
      msg.fail(500, lookup_err.get_message())
      return
    end

    # now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    json = [
    ]

    lookup.each do |o|
      json.push(o)
    end

    msg.reply(json)
  }

end
def place_order(msg)
  @mongo.save("orders", msg.body()) { |save_err,save|
    # error handling
    if (save_err != nil)
      msg.fail(500, save_err.get_message())
      return
    end

    msg.reply({
    })
  }
end
def load_data(db)
  db.drop_collection("albums") { |drop_err,drop|
    if (drop_err != nil)
      raise drop_err
    end

    albums = Java::JavaUtil::LinkedList.new()

    albums.push({
      'artist' => "The Wurzels",
      'genre' => "Scrumpy and Western",
      'title' => "I Am A Cider Drinker",
      'price' => 0.99
    })

    albums.push({
      'artist' => "Vanilla Ice",
      'genre' => "Hip Hop",
      'title' => "Ice Ice Baby",
      'price' => 0.01
    })

    albums.push({
      'artist' => "Ena Baga",
      'genre' => "Easy Listening",
      'title' => "The Happy Hammond",
      'price' => 0.5
    })


    albums.push({
      'artist' => "The Tweets",
      'genre' => "Bird related songs",
      'title' => "The Birdy Song",
      'price' => 1.2
    })

    albums.each do |album|
      db.insert("albums", album) { |res_err,res|
        puts "inserted #{JSON.generate(album)}"
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

# the app works 100% realtime
$vertx.event_bus().consumer("vtoons.listAlbums", &method(:list_albums))
$vertx.event_bus().consumer("vtoons.placeOrder", &method(:place_order))

router = VertxWeb::Router.router($vertx)

# We need cookies and sessions
router.route().handler(&VertxWeb::CookieHandler.create().method(:handle))
router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))
router.route().handler(&VertxWeb::SessionHandler.create(VertxWeb::LocalSessionStore.create($vertx)).method(:handle))

# Simple auth service which uses a properties file for user/role info
authProvider = VertxAuthShiro::ShiroAuth.create($vertx, :PROPERTIES, {
})

# We need a user session handler too to make sure the user is stored in the session between requests
router.route().handler(&VertxWeb::UserSessionHandler.create(authProvider).method(:handle))

router.post("/login").handler() { |ctx|
  credentials = ctx.get_body_as_json()
  if (credentials == nil)
    # bad request
    ctx.fail(400)
    return
  end

  # use the auth handler to perform the authentication for us
  authProvider.authenticate(credentials) { |login_err,login|
    # error handling
    if (login_err != nil)
      # forbidden
      ctx.fail(403)
      return
    end

    ctx.set_user(login)
    ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "application/json").end("{}")
  }
}

router.route("/eventbus/*").handler() { |ctx|
  # we need to be logged in
  if (ctx.user() == nil)
    ctx.fail(403)
  else
    ctx.next()
  end
}

# Allow outbound traffic to the vtoons addresses
options = {
  'inboundPermitteds' => [
    {
      'address' => "vtoons.listAlbums"
    },
    {
      'address' => "vtoons.login"
    },
    {
      'address' => "vtoons.placeOrder",
      'requiredAuthority' => "place_order"
    }
  ],
  'outboundPermitteds' => [
    {
    }
  ]
}

router.route("/eventbus/*").handler(&VertxWeb::SockJSHandler.create($vertx).bridge(options).method(:handle))

# Serve the static resources
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
