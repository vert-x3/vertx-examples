open module jpms.examples.tests {

  requires jpms.examples;
  requires org.junit.jupiter.api;
  requires testcontainers;
  requires io.vertx.client.sql.pg;
  requires io.vertx.client.sql;

  // Brought by testcontainers and required to make Idea happy
  requires junit;
  requires org.slf4j;
}
