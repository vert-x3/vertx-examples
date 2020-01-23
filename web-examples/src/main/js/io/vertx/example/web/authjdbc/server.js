var JDBCClient = require("vertx-jdbc-js/jdbc_client");
var Router = require("vertx-web-js/router");
var CookieHandler = require("vertx-web-js/cookie_handler");
var BodyHandler = require("vertx-web-js/body_handler");
var LocalSessionStore = require("vertx-web-js/local_session_store");
var SessionHandler = require("vertx-web-js/session_handler");
var JDBCAuth = require("vertx-auth-jdbc-js/jdbc_auth");
var UserSessionHandler = require("vertx-web-js/user_session_handler");
var RedirectAuthHandler = require("vertx-web-js/redirect_auth_handler");
var StaticHandler = require("vertx-web-js/static_handler");
var FormLoginHandler = require("vertx-web-js/form_login_handler");
var conn;
var setUpInitialData = function(url) {
  conn = Java.type("java.sql.DriverManager").getConnection(url);
  executeStatement("drop table if exists user;");
  executeStatement("drop table if exists user_roles;");
  executeStatement("drop table if exists roles_perms;");
  executeStatement("create table user (username varchar(255), password varchar(255), password_salt varchar(255) );");
  executeStatement("create table user_roles (username varchar(255), role varchar(255));");
  executeStatement("create table roles_perms (role varchar(255), perm varchar(255));");

  executeStatement("insert into user values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303863E493FBE906A9158B7D5DC');");
  executeStatement("insert into user_roles values ('tim', 'dev');");
  executeStatement("insert into user_roles values ('tim', 'admin');");
  executeStatement("insert into roles_perms values ('dev', 'commit_code');");
  executeStatement("insert into roles_perms values ('dev', 'eat_pizza');");
  executeStatement("insert into roles_perms values ('admin', 'merge_pr');");
};
var executeStatement = function(sql) {
  conn.createStatement().execute(sql);
};

// quick load of test data, this is a *sync* helper not intended for
// real deployments...
setUpInitialData("jdbc:hsqldb:mem:test?shutdown=true");

// Create a JDBC client with a test database
var client = JDBCClient.createShared(vertx, {
  "url" : "jdbc:hsqldb:mem:test?shutdown=true",
  "driver_class" : "org.hsqldb.jdbcDriver"
});

//     If you are planning NOT to build a fat jar, then use the BoneCP pool since it
//     can handle loading the jdbc driver classes from outside vert.x lib directory
//    JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
//        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.BoneCPDataSourceProvider")
//        .put("jdbcUrl", "jdbc:hsqldb:mem:test?shutdown=true")
//        .put("username", "sa")
//        .put("password", ""));

var router = Router.router(vertx);

// We need cookies, sessions and request bodies
router.route().handler(CookieHandler.create().handle);
router.route().handler(BodyHandler.create().handle);
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).handle);

// Simple auth service which uses a JDBC data source
var authProvider = JDBCAuth.create(vertx, client);

// We need a user session handler too to make sure the user is stored in the session between requests
router.route().handler(UserSessionHandler.create(authProvider).handle);

// Any requests to URI starting '/private/' require login
router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/loginpage.html").handle);

// Serve the static private pages from directory 'private'
router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private").handle);

// Handles the actual login
router.route("/loginhandler").handler(FormLoginHandler.create(authProvider).handle);

// Implement logout
router.route("/logout").handler(function (context) {
  context.clearUser();
  // Redirect back to the index page
  context.response().putHeader("location", "/").setStatusCode(302).end();
});

// Serve the non private static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.handle).listen(8080);
