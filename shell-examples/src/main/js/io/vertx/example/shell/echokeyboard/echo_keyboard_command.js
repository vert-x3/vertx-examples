var CommandBuilder = require("vertx-shell-js/command_builder");
var ShellService = require("vertx-shell-js/shell_service");

var starwars = CommandBuilder.command("echokeyboard").processHandler(function (process) {

  // Echo
  process.setStdin(function (keys) {
    process.write(keys.replace('\r', '\n'));
  });

  // Terminate when user hits Ctrl-C
  process.eventHandler('SIGINT', function (v) {
    process.end();
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
