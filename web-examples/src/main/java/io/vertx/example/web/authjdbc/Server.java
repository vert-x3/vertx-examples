package io.vertx.example.web.authjdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.jdbc.JDBCAuth;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // quick load of test data, this is a *sync* helper not intended for
    // real deployments...
    setUpInitialData("jdbc:hsqldb:mem:test?shutdown=true");

    // Create a JDBC client with a test database
    JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver"));


    Router router = Router.router(vertx);

    // We need cookies, sessions and request bodies
    router.route().handler(CookieHandler.create());
    router.route().handler(BodyHandler.create());
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

    // Simple auth service which uses a JDBC data source
    AuthProvider authProvider = JDBCAuth.create(client);

    // We need a user session handler too to make sure the user is stored in the session between requests
    router.route().handler(UserSessionHandler.create(authProvider));

    // Any requests to URI starting '/private/' require login
    router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/loginpage.html"));

    // Serve the static private pages from directory 'private'
    router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"));

    // Handles the actual login
    router.route("/loginhandler").handler(FormLoginHandler.create(authProvider));

    // Implement logout
    router.route("/logout").handler(context -> {
      context.clearUser();
      // Redirect back to the index page
      context.response().putHeader("location", "/").setStatusCode(302).end();
    });

    // Serve the non private static pages
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }

  private void setUpInitialData(String url) {
    List<String> SQL = new ArrayList<String>() {{
      add("drop table if exists user;");
      add("drop table if exists user_roles;");
      add("drop table if exists roles_perms;");
      add("create table user (username varchar(255), password varchar(255), password_salt varchar(255) );");
      add("create table user_roles (username varchar(255), role varchar(255));");
      add("create table roles_perms (role varchar(255), perm varchar(255));");

      add("insert into user values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303858E493FBE906A9158B7D5DC');");
      add("insert into user_roles values ('tim', 'dev');");
      add("insert into user_roles values ('tim', 'admin');");
      add("insert into roles_perms values ('dev', 'commit_code');");
      add("insert into roles_perms values ('dev', 'eat_pizza');");
      add("insert into roles_perms values ('admin', 'merge_pr');");
    }};

    try {
      Connection conn = DriverManager.getConnection(url);
      for (String sql : SQL) {
        conn.createStatement().execute(sql);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

