var Command = require("vertx-shell-js/command");
var ShellService = require("vertx-shell-js/shell_service");

var command = Command.builder("my-command").processHandler(function (process) {
  process.write("hello sir\n").end();
}).build();

var service = ShellService.create(vertx, {
  "telnetOptions" : {
    "host" : "localhost",
    "port" : 3000
  }
});
service.getCommandRegistry().registerCommand(command);
service.start();
