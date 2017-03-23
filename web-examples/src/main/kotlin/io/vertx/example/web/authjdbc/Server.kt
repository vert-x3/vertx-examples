package io.vertx.example.web.authjdbc

import io.vertx.ext.auth.jdbc.JDBCAuth
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.FormLoginHandler
import io.vertx.ext.web.handler.RedirectAuthHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.UserSessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.core.json.*

class Server : io.vertx.core.AbstractVerticle()  {
  var conn: java.sql.Connection
  fun setUpInitialData(url: String) {
    conn = java.sql.DriverManager.getConnection(url)
    executeStatement("drop table if exists user;")
    executeStatement("drop table if exists user_roles;")
    executeStatement("drop table if exists roles_perms;")
    executeStatement("create table user (username varchar(255), password varchar(255), password_salt varchar(255) );")
    executeStatement("create table user_roles (username varchar(255), role varchar(255));")
    executeStatement("create table roles_perms (role varchar(255), perm varchar(255));")

    executeStatement("insert into user values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303858E493FBE906A9158B7D5DC');")
    executeStatement("insert into user_roles values ('tim', 'dev');")
    executeStatement("insert into user_roles values ('tim', 'admin');")
    executeStatement("insert into roles_perms values ('dev', 'commit_code');")
    executeStatement("insert into roles_perms values ('dev', 'eat_pizza');")
    executeStatement("insert into roles_perms values ('admin', 'merge_pr');")
  }
  fun executeStatement(sql: String) {
    conn.createStatement().execute(sql)
  }
  override fun start() {

    // quick load of test data, this is a *sync* helper not intended for
    // real deployments...
    setUpInitialData("jdbc:hsqldb:mem:test?shutdown=true")

    // Create a JDBC client with a test database
    var client = JDBCClient.createShared(vertx, json {
      obj(
        "url" to "jdbc:hsqldb:mem:test?shutdown=true",
        "driver_class" to "org.hsqldb.jdbcDriver"
      )
    })

    //     If you are planning NOT to build a fat jar, then use the BoneCP pool since it
    //     can handle loading the jdbc driver classes from outside vert.x lib directory
    //    JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
    //        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.BoneCPDataSourceProvider")
    //        .put("jdbcUrl", "jdbc:hsqldb:mem:test?shutdown=true")
    //        .put("username", "sa")
    //        .put("password", ""));

    var router = Router.router(vertx)

    // We need cookies, sessions and request bodies
    router.route().handler(CookieHandler.create())
    router.route().handler(BodyHandler.create())
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

    // Simple auth service which uses a JDBC data source
    var authProvider = JDBCAuth.create(vertx, client)

    // We need a user session handler too to make sure the user is stored in the session between requests
    router.route().handler(UserSessionHandler.create(authProvider))

    // Any requests to URI starting '/private/' require login
    router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/loginpage.html"))

    // Serve the static private pages from directory 'private'
    router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"))

    // Handles the actual login
    router.route("/loginhandler").handler(FormLoginHandler.create(authProvider))

    // Implement logout
    router.route("/logout").handler({ context ->
      context.clearUser()
      // Redirect back to the index page
      context.response().putHeader("location", "/").setStatusCode(302).end()
    })

    // Serve the non private static pages
    router.route().handler(StaticHandler.create())

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}
