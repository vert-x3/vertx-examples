import groovy.transform.Field
import io.vertx.groovy.ext.mongo.MongoClient
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.groovy.ext.web.handler.StaticHandler
@Field def mongo
def loadData(db) {
  db.dropCollection("users", { drop ->
    if (drop.failed()) {
      throw new java.lang.RuntimeException(drop.cause())
    }

    def users = new java.util.LinkedList()

    users.add([
      username:"pmlopes",
      firstName:"Paulo",
      lastName:"Lopes",
      address:"The Netherlands"
    ])

    users.add([
      username:"timfox",
      firstName:"Tim",
      lastName:"Fox",
      address:"The Moon"
    ])

    users.each { user ->
      db.insert("users", user, { res ->
        println("inserted ${groovy.json.JsonOutput.toJson(user)}")
      })
    }
  })
}

// Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
mongo = MongoClient.createShared(vertx, [
  db_name:"demo"
])

// the load function just populates some data on the storage
this.loadData(mongo)

def router = Router.router(vertx)

router.route().handler(BodyHandler.create())

// define some REST API
router.get("/api/users").handler({ ctx ->
  mongo.find("users", [:], { lookup ->
    // error handling
    if (lookup.failed()) {
      ctx.fail(500)
      return
    }

    // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    def json = [
    ]

    lookup.result().each { o ->
      json.add(o)
    }

    ctx.response().putHeader(io.vertx.core.http.HttpHeaders.CONTENT_TYPE, "application/json")
    ctx.response().end(groovy.json.JsonOutput.toJson(json))
  })
})

router.get("/api/users/:id").handler({ ctx ->
  mongo.findOne("users", [
    _id:ctx.request().getParam("id")
  ], null, { lookup ->
    // error handling
    if (lookup.failed()) {
      ctx.fail(500)
      return
    }

    def user = lookup.result()

    if (user == null) {
      ctx.fail(404)
    } else {
      ctx.response().putHeader(io.vertx.core.http.HttpHeaders.CONTENT_TYPE, "application/json")
      ctx.response().end(groovy.json.JsonOutput.toJson(user))
    }
  })
})

router.post("/api/users").handler({ ctx ->
  def newUser = ctx.getBodyAsJson()

  mongo.findOne("users", [
    username:newUser.username
  ], null, { lookup ->
    // error handling
    if (lookup.failed()) {
      ctx.fail(500)
      return
    }

    def user = lookup.result()

    if (user != null) {
      // already exists
      ctx.fail(500)
    } else {
      mongo.insert("users", newUser, { insert ->
        // error handling
        if (insert.failed()) {
          ctx.fail(500)
          return
        }

        // add the generated id to the user object
        newUser._id = insert.result()

        ctx.response().putHeader(io.vertx.core.http.HttpHeaders.CONTENT_TYPE, "application/json")
        ctx.response().end(groovy.json.JsonOutput.toJson(newUser))
      })
    }
  })
})

router.put("/api/users/:id").handler({ ctx ->
  mongo.findOne("users", [
    _id:ctx.request().getParam("id")
  ], null, { lookup ->
    // error handling
    if (lookup.failed()) {
      ctx.fail(500)
      return
    }

    def user = lookup.result()

    if (user == null) {
      // does not exist
      ctx.fail(404)
    } else {

      // update the user properties
      def update = ctx.getBodyAsJson()

      user.username = update.username
      user.firstName = update.firstName
      user.lastName = update.lastName
      user.address = update.address

      mongo.replace("users", [
        _id:ctx.request().getParam("id")
      ], user, { replace ->
        // error handling
        if (replace.failed()) {
          ctx.fail(500)
          return
        }

        ctx.response().putHeader(io.vertx.core.http.HttpHeaders.CONTENT_TYPE, "application/json")
        ctx.response().end(groovy.json.JsonOutput.toJson(user))
      })
    }
  })
})

router.delete("/api/users/:id").handler({ ctx ->
  mongo.findOne("users", [
    _id:ctx.request().getParam("id")
  ], null, { lookup ->
    // error handling
    if (lookup.failed()) {
      ctx.fail(500)
      return
    }

    def user = lookup.result()

    if (user == null) {
      // does not exist
      ctx.fail(404)
    } else {

      mongo.remove("users", [
        _id:ctx.request().getParam("id")
      ], { remove ->
        // error handling
        if (remove.failed()) {
          ctx.fail(500)
          return
        }

        ctx.response().setStatusCode(204)
        ctx.response().end()
      })
    }
  })
})

// Create a router endpoint for the static content.
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
