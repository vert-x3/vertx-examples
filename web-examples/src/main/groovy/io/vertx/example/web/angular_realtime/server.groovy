import groovy.transform.Field
import io.vertx.groovy.ext.mongo.MongoClient
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.CookieHandler
import io.vertx.groovy.ext.web.handler.BodyHandler
import io.vertx.groovy.ext.web.sstore.LocalSessionStore
import io.vertx.groovy.ext.web.handler.SessionHandler
import io.vertx.ext.auth.shiro.ShiroAuthRealmType
import io.vertx.groovy.ext.auth.shiro.ShiroAuth
import io.vertx.groovy.ext.web.handler.UserSessionHandler
import io.vertx.groovy.ext.web.handler.sockjs.SockJSHandler
import io.vertx.groovy.ext.web.handler.StaticHandler
@Field def mongo
def listAlbums(msg) {
  // issue a find command to mongo to fetch all documents from the "albums" collection.
  mongo.find("albums", [:], { lookup ->
    // error handling
    if (lookup.failed()) {
      msg.fail(500, lookup.cause().getMessage())
      return
    }

    // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    def json = [
    ]

    lookup.result().each { o ->
      json.add(o)
    }

    msg.reply(json)
  })

}
def placeOrder(msg) {
  mongo.save("orders", msg.body(), { save ->
    // error handling
    if (save.failed()) {
      msg.fail(500, save.cause().getMessage())
      return
    }

    msg.reply([:])
  })
}
def loadData(db) {
  db.dropCollection("albums", { drop ->
    if (drop.failed()) {
      throw new java.lang.RuntimeException(drop.cause())
    }

    def albums = new java.util.LinkedList()

    albums.add([
      artist:"The Wurzels",
      genre:"Scrumpy and Western",
      title:"I Am A Cider Drinker",
      price:0.99d
    ])

    albums.add([
      artist:"Vanilla Ice",
      genre:"Hip Hop",
      title:"Ice Ice Baby",
      price:0.01d
    ])

    albums.add([
      artist:"Ena Baga",
      genre:"Easy Listening",
      title:"The Happy Hammond",
      price:0.5d
    ])


    albums.add([
      artist:"The Tweets",
      genre:"Bird related songs",
      title:"The Birdy Song",
      price:1.2d
    ])

    albums.each { album ->
      db.insert("albums", album, { res ->
        println("inserted ${groovy.json.JsonOutput.toJson(album)}")
      })
    }
  })
}

vertx.deployVerticle("maven:io.vertx:vertx-mongo-embedded-db:3.0.0-milestone6", { done ->
  // Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
  mongo = MongoClient.createShared(vertx, [
    db_name:"demo"
  ])

  // the load function just populates some data on the storage
  this.loadData(mongo)

  // the app works 100% realtime
  vertx.eventBus().consumer("vtoons.listAlbums", this.&listAlbums)
  vertx.eventBus().consumer("vtoons.placeOrder", this.&placeOrder)

  def router = Router.router(vertx)

  // We need cookies and sessions
  router.route().handler(CookieHandler.create())
  router.route().handler(BodyHandler.create())
  router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

  // Simple auth service which uses a properties file for user/role info
  def authProvider = ShiroAuth.create(vertx, ShiroAuthRealmType.PROPERTIES, [:])

  // We need a user session handler too to make sure the user is stored in the session between requests
  router.route().handler(UserSessionHandler.create(authProvider))

  router.post("/login").handler({ ctx ->
    def credentials = ctx.getBodyAsJson()
    if (credentials == null) {
      // bad request
      ctx.fail(400)
      return
    }

    // use the auth handler to perform the authentication for us
    authProvider.authenticate(credentials, { login ->
      // error handling
      if (login.failed()) {
        // forbidden
        ctx.fail(403)
        return
      }

      ctx.setUser(login.result())
      ctx.response().putHeader(io.vertx.core.http.HttpHeaders.CONTENT_TYPE, "application/json").end("{}")
    })
  })

  router.route("/eventbus/*").handler({ ctx ->
    // we need to be logged in
    if (ctx.user() == null) {
      ctx.fail(403)
    } else {
      ctx.next()
    }
  })

  // Allow outbound traffic to the vtoons addresses
  def options = [
    inboundPermitteds:[
      [
        address:"vtoons.listAlbums"
      ],
      [
        address:"vtoons.login"
      ],
      [
        address:"vtoons.placeOrder",
        requiredAuthority:"place_order"
      ]
    ],
    outboundPermitteds:[
      [:]
    ]
  ]

  router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options))

  // Serve the static resources
  router.route().handler(StaticHandler.create())

  vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
})
