import io.vertx.ceylon.core {
  Verticle
}
import io.vertx.ceylon.core.http {
  HttpClientResponse
}
class Client() extends Verticle() {
  
  shared actual void start() {
    vertx.createHttpClient().getNow(8080, "localhost", "/", (HttpClientResponse|Throwable resp) {
      assert(is HttpClientResponse resp);
      print("Got response ``resp.statusCode()``");
      resp.bodyHandler((body) => print("Got data ``body.toString("ISO-8859-1")``"));
    });
  }  
}