require 'vertx-jdbc/jdbc_client'
require 'vertx-web/router'
require 'vertx-web/cookie_handler'
require 'vertx-web/body_handler'
require 'vertx-web/local_session_store'
require 'vertx-web/session_handler'
require 'vertx-auth-jdbc/jdbc_auth'
require 'vertx-web/user_session_handler'
require 'vertx-web/redirect_auth_handler'
require 'vertx-web/static_handler'
require 'vertx-web/form_login_handler'
@conn
def set_up_initial_data(url)
  @conn = Java::JavaSql::DriverManager.get_connection(url)
  execute_statement("drop table if exists user;")
  execute_statement("drop table if exists user_roles;")
  execute_statement("drop table if exists roles_perms;")
  execute_statement("create table user (username varchar(255), password varchar(255), password_salt varchar(255) );")
  execute_statement("create table user_roles (username varchar(255), role varchar(255));")
  execute_statement("create table roles_perms (role varchar(255), perm varchar(255));")

  execute_statement("insert into user values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303863E493FBE906A9158B7D5DC');")
  execute_statement("insert into user_roles values ('tim', 'dev');")
  execute_statement("insert into user_roles values ('tim', 'admin');")
  execute_statement("insert into roles_perms values ('dev', 'commit_code');")
  execute_statement("insert into roles_perms values ('dev', 'eat_pizza');")
  execute_statement("insert into roles_perms values ('admin', 'merge_pr');")
end
def execute_statement(sql)
  @conn.create_statement().execute?(sql)
end

# quick load of test data, this is a *sync* helper not intended for
# real deployments...
set_up_initial_data("jdbc:hsqldb:mem:test?shutdown=true")

# Create a JDBC client with a test database
client = VertxJdbc::JDBCClient.create_shared($vertx, {
  'url' => "jdbc:hsqldb:mem:test?shutdown=true",
  'driver_class' => "org.hsqldb.jdbcDriver"
})

#     If you are planning NOT to build a fat jar, then use the BoneCP pool since it
#     can handle loading the jdbc driver classes from outside vert.x lib directory
#    JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
#        .put("provider_class", "io.vertx.ext.jdbc.spi.impl.BoneCPDataSourceProvider")
#        .put("jdbcUrl", "jdbc:hsqldb:mem:test?shutdown=true")
#        .put("username", "sa")
#        .put("password", ""));

router = VertxWeb::Router.router($vertx)

# We need cookies, sessions and request bodies
router.route().handler(&VertxWeb::CookieHandler.create().method(:handle))
router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))
router.route().handler(&VertxWeb::SessionHandler.create(VertxWeb::LocalSessionStore.create($vertx)).method(:handle))

# Simple auth service which uses a JDBC data source
authProvider = VertxAuthJdbc::JDBCAuth.create($vertx, client)

# We need a user session handler too to make sure the user is stored in the session between requests
router.route().handler(&VertxWeb::UserSessionHandler.create(authProvider).method(:handle))

# Any requests to URI starting '/private/' require login
router.route("/private/*").handler(&VertxWeb::RedirectAuthHandler.create(authProvider, "/loginpage.html").method(:handle))

# Serve the static private pages from directory 'private'
router.route("/private/*").handler(&VertxWeb::StaticHandler.create().set_caching_enabled(false).set_web_root("private").method(:handle))

# Handles the actual login
router.route("/loginhandler").handler(&VertxWeb::FormLoginHandler.create(authProvider).method(:handle))

# Implement logout
router.route("/logout").handler() { |context|
  context.clear_user()
  # Redirect back to the index page
  context.response().put_header("location", "/").set_status_code(302).end()
}

# Serve the non private static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:handle)).listen(8080)
