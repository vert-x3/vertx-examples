var Command = require("vertx-shell-js/command");
var ShellService = require("vertx-shell-js/shell_service");

var helloWorld = Command.builder("hello-world").processHandler(function (process) {
  process.write("hello world\n").end();
}).build();

var service = ShellService.create(vertx, {
  "telnetOptions" : {
    "host" : "localhost",
    "port" : 3000
  }
});
service.getCommandRegistry().registerCommand(helloWorld);
service.start(function (ar, ar_err) {
  if (!ar_err == null) {
    ar_err.printStackTrace();
  }
});
