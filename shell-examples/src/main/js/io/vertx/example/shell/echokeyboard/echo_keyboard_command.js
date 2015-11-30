var CommandBuilder = require("vertx-shell-js/command_builder");
var ShellService = require("vertx-shell-js/shell_service");
var CommandRegistry = require("vertx-shell-js/command_registry");

var starwars = CommandBuilder.command("echokeyboard").processHandler(function (process) {

  // Echo
  process.setStdin(function (keys) {
    process.write(keys.replace('\r', '\n'));
  });

  // Terminate when user hits Ctrl-C
  process.interruptHandler(function (v) {
    process.end();
  });

}).build(vertx);

var service = ShellService.create(vertx, {
  "telnetOptions" : {
    "host" : "localhost",
    "port" : 3000
  }
});
CommandRegistry.getShared(vertx).registerCommand(starwars);
service.start(function (ar, ar_err) {
  if (!ar_err == null) {
    ar_err.printStackTrace();
  }
});
