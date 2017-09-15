import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.sql.ResultSet
import io.vertx.ext.sql.SQLConnection
import io.vertx.ext.sql.UpdateResult
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.core.json.*
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch


fun main(args : Array<String>) {
  val vertx = Vertx.vertx()
  vertx.deployVerticle(MainVerticle())
}

class MainVerticle : CoroutineVerticle() {

  private lateinit var client : JDBCClient

  suspend override fun start() {

    client = JDBCClient.createShared(vertx, json {
      obj(
        "url" to "jdbc:hsqldb:mem:test?shutdown=true",
        "driver_class" to "org.hsqldb.jdbcDriver",
        "max_pool_size-loop" to 30
      )
    })

    // Populate database
    val statements = listOf(
      "CREATE TABLE MOVIE (ID INTEGER PRIMARY KEY, TITLE VARCHAR(256) NOT NULL)",
      "CREATE TABLE RATING (ID INTEGER IDENTITY PRIMARY KEY, value INTEGER, MOVIE INTEGER)",
      "INSERT INTO MOVIE (ID, TITLE) VALUES 1, 'Star Wars'",
      "INSERT INTO RATING (VALUE, MOVIE) VALUES 1, 1",
      "INSERT INTO RATING (VALUE, MOVIE) VALUES 5, 1",
      "INSERT INTO RATING (VALUE, MOVIE) VALUES 9, 1",
      "INSERT INTO RATING (VALUE, MOVIE) VALUES 10, 1"
      )

    val connection = awaitResult<SQLConnection> { client.getConnection(it) }
    try {
      for (statement in statements) {
        awaitResult<Void> { connection.execute(statement, it) }
      }
    } finally {
      connection.close()
    }

    val router = Router.router(vertx)

    router.get("/movie/:id").coroutineHandler { ctx ->
      val id = Integer.parseInt(ctx.pathParam("id"))
      val result = awaitResult<ResultSet> { client.queryWithParams("SELECT TITLE FROM MOVIE WHERE ID=?", json { array(id) }, it) }
      if (result.rows.size == 1) {
        ctx.response().end(json {
          obj("id" to id, "title" to result.rows[0]["TITLE"]).encode()
        })
      } else {
        ctx.response().setStatusCode(404).end()
      }
    }

    router.post("/rate/:id").coroutineHandler { ctx ->
      val movie = Integer.parseInt(ctx.pathParam("id"))
      val rating = Integer.parseInt(ctx.queryParam("value")[0])
      val connection = awaitResult<SQLConnection> { client.getConnection(it) }
      try {
        val result = awaitResult<ResultSet> { connection.queryWithParams("SELECT TITLE FROM MOVIE WHERE ID=?", json { array(movie) }, it) }
        if (result.rows.size == 1) {
          awaitResult<UpdateResult> { connection.updateWithParams("INSERT INTO RATING (VALUE, MOVIE) VALUES ?, ?", json { array(rating, movie) }, it) }
          ctx.response().setStatusCode(200).end()
        } else {
          ctx.response().setStatusCode(404).end()
        }
      } finally {
        connection.close()
      }
    }

    router.get("/rating/:id").coroutineHandler { ctx ->
      val id = Integer.parseInt(ctx.pathParam("id"))
      val result = awaitResult<ResultSet> { client.queryWithParams("SELECT AVG(VALUE) AS VALUE FROM RATING WHERE MOVIE=?", json { array(id) }, it) }
      ctx.response().end(json {
        obj("id" to id, "rating" to result.rows[0]["VALUE"]).encode()
      })
    }

    awaitResult<HttpServer> { vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(config.getInteger("http.port", 8080), it)
    }

    println("Application started")
  }
}

fun Route.coroutineHandler(fn : suspend (RoutingContext) -> Unit) {
  handler { ctx ->
    launch(ctx.vertx().dispatcher()) {
      try {
        fn(ctx)
      } catch(e: Exception) {
        ctx.fail(e)
      }
    }
  }
}
