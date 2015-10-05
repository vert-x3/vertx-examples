var CommandBuilder = require("vertx-shell-js/command_builder");
var ShellService = require("vertx-shell-js/shell_service");

var starwars = CommandBuilder.command("starwars").processHandler(function (process) {

  // Connect the client
  var client = process.vertx().createNetClient();
  client.connect(23, "towel.blinkenlights.nl", function (ar, ar_err) {
    if (ar_err == null) {
      var socket = ar;

      // Ctrl-C closes the socket
      process.eventHandler('SIGINT', function (v) {
        socket.close();
      });

      //
      socket.handler(function (buff) {
        // Push the data to the Shell
        process.write(buff.toString("UTF-8"));
      }).exceptionHandler(function (err) {
        err.printStackTrace();
        socket.close();
      });

      // When socket closes, end the command
      socket.closeHandler(function (v) {
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
