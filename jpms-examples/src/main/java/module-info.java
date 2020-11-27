// Opening the module allows Vertx to access the server-keystore.jks via the classloader
open module jpms.examples {

  requires com.fasterxml.jackson.core;
  requires io.vertx.core;
  requires io.vertx.web;

  requires io.vertx.client.sql;
  requires io.vertx.client.jdbc;
  requires java.sql;

}
