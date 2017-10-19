package movierating

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


class App : CoroutineVerticle() {

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
      "CREATE TABLE MOVIE (ID VARCHAR(16) PRIMARY KEY, TITLE VARCHAR(256) NOT NULL)",
      "CREATE TABLE RATING (ID INTEGER IDENTITY PRIMARY KEY, value INTEGER, MOVIE_ID VARCHAR(16))",
      "INSERT INTO MOVIE (ID, TITLE) VALUES 'starwars', 'Star Wars'",
      "INSERT INTO MOVIE (ID, TITLE) VALUES 'indianajones', 'Indiana Jones'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 1, 'starwars'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 5, 'starwars'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 9, 'starwars'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 10, 'starwars'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 4, 'indianajones'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 7, 'indianajones'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 3, 'indianajones'",
      "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 9, 'indianajones'"
      )
    val connection = awaitResult<SQLConnection> { client.getConnection(it) }
    connection.use {
      for (statement in statements) {
        awaitResult<Void> { connection.execute(statement, it) }
      }
    }

    // Build Vert.x Web router
    val router = Router.router(vertx)
    router.get("/movie/:id").coroutineHandler { ctx -> getMovie(ctx) }
    router.post("/rateMovie/:id").coroutineHandler { ctx -> rateMovie(ctx) }
    router.get("/getRating/:id").coroutineHandler { ctx -> getRating(ctx) }

    // Start the server
    awaitResult<HttpServer> { vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(config.getInteger("http.port", 8080), it)
    }
  }

  // Send info about a movie
  suspend fun getMovie(ctx: RoutingContext) {
    val id = ctx.pathParam("id")
    val result = awaitResult<ResultSet> { client.queryWithParams("SELECT TITLE FROM MOVIE WHERE ID=?", json { array(id) }, it) }
    if (result.rows.size == 1) {
      ctx.response().end(json {
        obj("id" to id, "title" to result.rows[0]["TITLE"]).encode()
      })
    } else {
      ctx.response().setStatusCode(404).end()
    }
  }

  // Rate a movie
  suspend fun rateMovie(ctx: RoutingContext) {
    val movie = ctx.pathParam("id")
    val rating = Integer.parseInt(ctx.queryParam("getRating")[0])
    val connection = awaitResult<SQLConnection> { client.getConnection(it) }
    connection.use {
      val result = awaitResult<ResultSet> { connection.queryWithParams("SELECT TITLE FROM MOVIE WHERE ID=?", json { array(movie) }, it) }
      if (result.rows.size == 1) {
        awaitResult<UpdateResult> { connection.updateWithParams("INSERT INTO RATING (VALUE, MOVIE_ID) VALUES ?, ?", json { array(rating, movie) }, it) }
        ctx.response().setStatusCode(200).end()
      } else {
        ctx.response().setStatusCode(404).end()
      }
    }
  }

  // Get the current rating of a movie
  suspend fun getRating(ctx: RoutingContext) {
    val id = ctx.pathParam("id")
    val result = awaitResult<ResultSet> { client.queryWithParams("SELECT AVG(VALUE) AS VALUE FROM RATING WHERE MOVIE_ID=?", json { array(id) }, it) }
    ctx.response().end(json {
      obj("id" to id, "getRating" to result.rows[0]["VALUE"]).encode()
    })
  }
}

/**
 * An extension method for simplifying coroutines usage with Vert.x Web routers
 */
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
