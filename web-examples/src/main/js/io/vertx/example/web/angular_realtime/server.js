var MongoClient = require("vertx-mongo-js/mongo_client");
var Router = require("vertx-web-js/router");
var CookieHandler = require("vertx-web-js/cookie_handler");
var BodyHandler = require("vertx-web-js/body_handler");
var LocalSessionStore = require("vertx-web-js/local_session_store");
var SessionHandler = require("vertx-web-js/session_handler");
var ShiroAuth = require("vertx-auth-shiro-js/shiro_auth");
var UserSessionHandler = require("vertx-web-js/user_session_handler");
var SockJSHandler = require("vertx-web-js/sock_js_handler");
var StaticHandler = require("vertx-web-js/static_handler");
var mongo;
var listAlbums = function(msg) {
  // issue a find command to mongo to fetch all documents from the "albums" collection.
  mongo.find("albums", {
  }, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      msg.fail(500, lookup_err.getMessage());
      return
    }

    // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    var json = [
    ];

    Array.prototype.forEach.call(lookup, function(o) {
      json.push(o);
    });

    msg.reply(json);
  });

};
var placeOrder = function(msg) {
  mongo.save("orders", msg.body(), function (save, save_err) {
    // error handling
    if (save_err != null) {
      msg.fail(500, save_err.getMessage());
      return
    }

    msg.reply({
    });
  });
};
var loadData = function(db) {
  db.dropCollection("albums", function (drop, drop_err) {
    if (drop_err != null) {
      throw drop_err;
    }

    var albums = new (Java.type("java.util.LinkedList"))();

    albums.push({
      "artist" : "The Wurzels",
      "genre" : "Scrumpy and Western",
      "title" : "I Am A Cider Drinker",
      "price" : 0.99
    });

    albums.push({
      "artist" : "Vanilla Ice",
      "genre" : "Hip Hop",
      "title" : "Ice Ice Baby",
      "price" : 0.01
    });

    albums.push({
      "artist" : "Ena Baga",
      "genre" : "Easy Listening",
      "title" : "The Happy Hammond",
      "price" : 0.5
    });


    albums.push({
      "artist" : "The Tweets",
      "genre" : "Bird related songs",
      "title" : "The Birdy Song",
      "price" : 1.2
    });

    Array.prototype.forEach.call(albums, function(album) {
      db.insert("albums", album, function (res, res_err) {
        console.log("inserted " + JSON.stringify(album));
      });
    });
  });
};

vertx.deployVerticle("maven:io.vertx:vertx-mongo-embedded-db:3.0.0-milestone6", function (done, done_err) {
  // Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
  mongo = MongoClient.createShared(vertx, {
    "db_name" : "demo"
  });

  // the load function just populates some data on the storage
  loadData(mongo);

  // the app works 100% realtime
  vertx.eventBus().consumer("vtoons.listAlbums", listAlbums);
  vertx.eventBus().consumer("vtoons.placeOrder", placeOrder);

  var router = Router.router(vertx);

  // We need cookies and sessions
  router.route().handler(CookieHandler.create().handle);
  router.route().handler(BodyHandler.create().handle);
  router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).handle);

  // Simple auth service which uses a properties file for user/role info
  var authProvider = ShiroAuth.create(vertx, 'PROPERTIES', {
  });

  // We need a user session handler too to make sure the user is stored in the session between requests
  router.route().handler(UserSessionHandler.create(authProvider).handle);

  router.post("/login").handler(function (ctx) {
    var credentials = ctx.getBodyAsJson();
    if (credentials === null) {
      // bad request
      ctx.fail(400);
      return
    }

    // use the auth handler to perform the authentication for us
    authProvider.authenticate(credentials, function (login, login_err) {
      // error handling
      if (login_err != null) {
        // forbidden
        ctx.fail(403);
        return
      }

      ctx.setUser(login);
      ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "application/json").end("{}");
    });
  });

  router.route("/eventbus/*").handler(function (ctx) {
    // we need to be logged in
    if (ctx.user() === null) {
      ctx.fail(403);
    } else {
      ctx.next();
    }
  });

  // Allow outbound traffic to the vtoons addresses
  var options = {
    "inboundPermitteds" : [
      {
        "address" : "vtoons.listAlbums"
      },
      {
        "address" : "vtoons.login"
      },
      {
        "address" : "vtoons.placeOrder",
        "requiredAuthority" : "place_order"
      }
    ],
    "outboundPermitteds" : [
      {
      }
    ]
  };

  router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options).handle);

  // Serve the static resources
  router.route().handler(StaticHandler.create().handle);

  vertx.createHttpServer().requestHandler(router.accept).listen(8080);
});
