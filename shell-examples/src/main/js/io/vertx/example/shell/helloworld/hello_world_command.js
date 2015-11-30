var CommandBuilder = require("vertx-shell-js/command_builder");
var ShellService = require("vertx-shell-js/shell_service");
var CommandRegistry = require("vertx-shell-js/command_registry");

var helloWorld = CommandBuilder.command("hello-world").processHandler(function (process) {
  process.write("hello world\n");
  process.end();
}).build(vertx);

var service = ShellService.create(vertx, {
  "telnetOptions" : {
    "host" : "localhost",
    "port" : 3000
  }
});
CommandRegistry.getShared(vertx).registerCommand(helloWorld);
service.start(function (ar, ar_err) {
  if (!ar_err == null) {
    ar_err.printStackTrace();
  }
});
