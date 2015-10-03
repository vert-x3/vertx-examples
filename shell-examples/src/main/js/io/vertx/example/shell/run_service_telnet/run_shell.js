var ShellService = require("vertx-shell-js/shell_service");
var service = ShellService.create(vertx, {
  "telnetOptions" : {
    "host" : "localhost",
    "port" : 3000
  }
});
service.start();
