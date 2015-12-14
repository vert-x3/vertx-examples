import io.vertx.ceylon.core { ... }
import io.vertx.ceylon.core.http { ... }

String index = """<html>
                  <head><title>Web Socket Test</title></head>
                  <body>
                  <script>
                      var socket;
                      if (window.WebSocket) {
                          socket = new WebSocket("ws://localhost:8080/myapp");
                          socket.onmessage = function (event) {
                              alert("Received data from websocket: " + event.data);
                          }
                          socket.onopen = function (event) {
                              alert("Web Socket opened!");
                          };
                          socket.onclose = function (event) {
                              alert("Web Socket closed.");
                          };
                      } else {
                          alert("Your browser does not support Websockets. (Use Chrome)");
                      }
                      function send(message) {
                          if (!window.WebSocket) {
                              return;
                          }
                          if (socket.readyState == WebSocket.OPEN) {
                              socket.send(message);
                          } else {
                              alert("The socket is not open.");
                          }
                      }
                  </script>
                  <form onsubmit="return false;">
                      <input type="text" name="message" value="Hello, World!"/>
                      <input type="button" value="Send Web Socket Data" onclick="send(this.form.message.value)"/>
                  </form>
                  </body>
                  </html>""";

shared class Server() extends Verticle() {
  
  shared actual void start() {
    value server = vertx.createHttpServer();
    server.requestHandler((HttpServerRequest req) => 
      req.response().
        putHeader("content-type", "text/html").
        end(index)
    ).websocketHandler((ServerWebSocket ws) =>
      ws.handler(ws.writeBinaryMessage)).listen(8080);
  }
}