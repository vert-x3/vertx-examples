import io.vertx.ceylon.core {
  Verticle
}
import io.vertx.ceylon.core.buffer {
  Buffer, buffer
}
import io.vertx.ceylon.core.http {
  WebSocket
}
class Client() extends Verticle() {
  
  shared actual void start() {
    value client = vertx.createHttpClient();
    client.websocket(8080, "localhost", "/some-uri", (WebSocket websocket) {
      websocket.handler((Buffer data) {
        print("Received data ``data.toString("ISO-8859-1")``");
        client.close();
      });
      websocket.writeBinaryMessage(buffer.buffer("Hello World"));
    });
  }  
}