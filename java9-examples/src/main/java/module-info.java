// Opening the module allows Vertx to access the server-keystore.jks via the classloader
open module java9.examples {

  requires vertx.core;
  requires vertx.web;

  requires vertx.jdbc.client;
  requires vertx.sql.common;
  requires java.sql;

}
