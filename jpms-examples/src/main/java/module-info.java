// Opening the module allows Vertx to access the server-keystore.jks via the classloader
open module jpms.examples {

  requires com.fasterxml.jackson.core;

  requires io.vertx.core;
  requires io.vertx.grpc.common;
  requires io.vertx.grpc.server;

  requires io.vertx.sql.client;
  requires io.vertx.sql.client.pg;
  requires java.sql;

  requires static io.vertx.serviceproxy;

  requires io.netty.tcnative.classes.openssl;

  requires com.google.protobuf;

  // Brotli4J
  requires com.aayushatharva.brotli4j;

  // SSL
  requires jdk.crypto.ec;
  requires io.netty.codec.compression;

  // Service proxy
  requires io.vertx.codegen.api;
  requires io.vertx.codegen.json;

  // SQL client template
  requires io.vertx.sql.client.templates;
  requires io.vertx.grpc.reflection;

  exports io.vertx.example.jpms.sqlclient;
  exports io.vertx.example.jpms.sqltemplate;
  exports io.vertx.example.jpms.native_transport;
  exports io.vertx.example.jpms.serviceproxy;

}
