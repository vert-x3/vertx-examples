package movierating

import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.ext.sql.executeAwait
import io.vertx.kotlin.ext.sql.getConnectionAwait
import io.vertx.kotlin.ext.sql.queryWithParamsAwait
import io.vertx.kotlin.ext.sql.updateWithParamsAwait
import kotlinx.coroutines.launch


class App : CoroutineVerticle() {

  private lateinit var client: JDBCClient

  override suspend fun start() {

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
    client.getConnectionAwait()
      .use { connection -> statements.forEach { connection.executeAwait(it) } }

    // Build Vert.x Web router
    val router = Router.router(vertx)
    router.get("/movie/:id").coroutineHandler { ctx -> getMovie(ctx) }
    router.post("/rateMovie/:id").coroutineHandler { ctx -> rateMovie(ctx) }
    router.get("/getRating/:id").coroutineHandler { ctx -> getRating(ctx) }

    // Start the server
    vertx.createHttpServer()
        .requestHandler(router)
        .listenAwait(config.getInteger("http.port", 8080))
  }

  // Send info about a movie
  suspend fun getMovie(ctx: RoutingContext) {
    val id = ctx.pathParam("id")
    val result = client.queryWithParamsAwait("SELECT TITLE FROM MOVIE WHERE ID=?", json { array(id) })
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
    client.getConnectionAwait().use { connection ->
      val result = connection.queryWithParamsAwait("SELECT TITLE FROM MOVIE WHERE ID=?", json { array(movie) })
      if (result.rows.size == 1) {
        connection.updateWithParamsAwait("INSERT INTO RATING (VALUE, MOVIE_ID) VALUES ?, ?", json { array(rating, movie) })
        ctx.response().setStatusCode(200).end()
      } else {
        ctx.response().setStatusCode(404).end()
      }
    }
  }

  // Get the current rating of a movie
  suspend fun getRating(ctx: RoutingContext) {
    val id = ctx.pathParam("id")
    val result = client.queryWithParamsAwait("SELECT AVG(VALUE) AS VALUE FROM RATING WHERE MOVIE_ID=?", json { array(id) })
    ctx.response().end(json {
      obj("id" to id, "getRating" to result.rows[0]["VALUE"]).encode()
    })
  }

  /**
   * An extension method for simplifying coroutines usage with Vert.x Web routers
   */
  fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
    handler { ctx ->
      launch(ctx.vertx().dispatcher()) {
        try {
          fn(ctx)
        } catch (e: Exception) {
          ctx.fail(e)
        }
      }
    }
  }
}
