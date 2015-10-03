var Command = require("vertx-shell-js/command");
var ShellService = require("vertx-shell-js/shell_service");

var starwars = Command.builder("starwars").processHandler(function (process) {
  var client = process.vertx().createNetClient();
  client.connect(23, "towel.blinkenlights.nl", function (ar, ar_err) {
    if (ar_err == null) {
      var socket = ar;
      process.eventHandler('SIGINT', function (v) {
        socket.close();
        process.end();
      });
      socket.handler(function (buff) {
        process.write(buff.toString("UTF-8"));
      }).exceptionHandler(function (err) {
        err.printStackTrace();
        process.end();
      }).endHandler(function (v) {
        process.end();
      });
    } else {
      process.write("Could not connect to remote Starwars server\n").end();
    }
  });
}).build();

var service = ShellService.create(vertx, {
  "telnetOptions" : {
    "host" : "localhost",
    "port" : 3000
  }
});
service.getCommandRegistry().registerCommand(starwars);
service.start(function (ar, ar_err) {
  if (!ar_err == null) {
    ar_err.printStackTrace();
  }
});
