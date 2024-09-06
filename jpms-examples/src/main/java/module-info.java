// Opening the module allows Vertx to access the server-keystore.jks via the classloader
open module jpms.examples {

  requires com.fasterxml.jackson.core;

  requires io.vertx.grpc.server;

  requires io.vertx.client.sql;
  requires io.vertx.client.sql.pg;
  requires java.sql;

  // Is that actually necessary
  requires com.ongres.scram.client;
  requires com.ongres.scram.common;

  requires com.google.protobuf;

  // SSL
  requires jdk.crypto.ec;

  exports io.vertx.example.jpms.sqlclient;

}
