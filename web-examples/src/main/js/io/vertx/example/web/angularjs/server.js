var MongoClient = require("vertx-mongo-js/mongo_client");
var Router = require("vertx-web-js/router");
var BodyHandler = require("vertx-web-js/body_handler");
var StaticHandler = require("vertx-web-js/static_handler");
var mongo;
var loadData = function(db) {
  db.dropCollection("users", function (drop, drop_err) {
    if (drop_err != null) {
      throw drop_err;
    }

    var users = new (Java.type("java.util.LinkedList"))();

    users.push({
      "username" : "pmlopes",
      "firstName" : "Paulo",
      "lastName" : "Lopes",
      "address" : "The Netherlands"
    });

    users.push({
      "username" : "timfox",
      "firstName" : "Tim",
      "lastName" : "Fox",
      "address" : "The Moon"
    });

    Array.prototype.forEach.call(users, function(user) {
      db.insert("users", user, function (res, res_err) {
        console.log("inserted " + JSON.stringify(user));
      });
    });
  });
};

// Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
mongo = MongoClient.createShared(vertx, {
  "db_name" : "demo"
});

// the load function just populates some data on the storage
loadData(mongo);

var router = Router.router(vertx);

router.route().handler(BodyHandler.create().handle);

// define some REST API
router.get("/api/users").handler(function (ctx) {
  mongo.find("users", {
  }, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(500);
      return
    }

    // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    var json = [
    ];

    Array.prototype.forEach.call(lookup, function(o) {
      json.push(o);
    });

    ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "application/json");
    ctx.response().end(JSON.stringify(json));
  });
});

router.get("/api/users/:id").handler(function (ctx) {
  mongo.findOne("users", {
    "id" : ctx.request().getParam("id")
  }, null, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(500);
      return
    }

    var user = lookup;

    if (user === null) {
      ctx.fail(404);
    } else {
      ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "application/json");
      ctx.response().end(JSON.stringify(user));
    }
  });
});

router.post("/api/users").handler(function (ctx) {
  var newUser = ctx.getBodyAsJson();

  mongo.findOne("users", {
    "username" : newUser.username
  }, null, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(500);
      return
    }

    var user = lookup;

    if (user !== null) {
      // already exists
      ctx.fail(500);
    } else {
      mongo.insert("users", newUser, function (insert, insert_err) {
        // error handling
        if (insert_err != null) {
          ctx.fail(500);
          return
        }

        // add the generated id to the user object
        newUser.id = insert;

        ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "application/json");
        ctx.response().end(JSON.stringify(newUser));
      });
    }
  });
});

router.put("/api/users/:id").handler(function (ctx) {
  mongo.findOne("users", {
    "id" : ctx.request().getParam("id")
  }, null, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(500);
      return
    }

    var user = lookup;

    if (user === null) {
      // does not exist
      ctx.fail(404);
    } else {

      // update the user properties
      var update = ctx.getBodyAsJson();

      user.username = update.username;
      user.firstName = update.firstName;
      user.lastName = update.lastName;
      user.address = update.address;

      mongo.replace("users", {
        "id" : ctx.request().getParam("id")
      }, user, function (replace, replace_err) {
        // error handling
        if (replace_err != null) {
          ctx.fail(500);
          return
        }

        ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "application/json");
        ctx.response().end(JSON.stringify(user));
      });
    }
  });
});

router.delete("/api/users/:id").handler(function (ctx) {
  mongo.findOne("users", {
    "id" : ctx.request().getParam("id")
  }, null, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(500);
      return
    }

    var user = lookup;

    if (user === null) {
      // does not exist
      ctx.fail(404);
    } else {

      mongo.remove("users", {
        "id" : ctx.request().getParam("id")
      }, function (remove, remove_err) {
        // error handling
        if (remove_err != null) {
          ctx.fail(500);
          return
        }

        ctx.response().setStatusCode(204);
        ctx.response().end();
      });
    }
  });
});

// Create a router endpoint for the static content.
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);
