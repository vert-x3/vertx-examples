var CLI = require("vertx-js/cli");
var CommandBuilder = require("vertx-shell-js/command_builder");
var ShellService = require("vertx-shell-js/shell_service");
var CommandRegistry = require("vertx-shell-js/command_registry");

// Create the wget CLI
var cli = CLI.create("wget").setSummary("Wget implemented with Vert.x HTTP client").addArgument({
  "index" : 0,
  "argName" : "http-url",
  "description" : "the HTTP uri to get"
});

// Create the command
var helloWorld = CommandBuilder.command(cli).processHandler(function (process) {
  var url;
  try {
    url = new (Java.type("java.net.URL"))(process.commandLine().getArgumentValue(0));
  } catch(err) {
    process.write("Bad url\n").end();
    return
  }

  var client = process.vertx().createHttpClient();
  process.write("Connecting to " + url + "\n");
  var port = url.getPort();
  if (port === -1) {
    port = 80;
  }
  var req = client.get(port, url.getHost(), url.getPath());
  req.exceptionHandler(function (err) {
    process.write("wget: error " + err.getMessage() + "\n");
    process.end();
  });
  req.handler(function (resp) {
    process.write(resp.statusCode() + " " + resp.statusMessage() + "\n");
    var contentType = resp.getHeader("Content-Type");
    var contentLength = resp.getHeader("Content-Length");
    process.write("Length: " + (contentLength !== null ? contentLength : "unspecified"));
    if (contentType !== null) {
      process.write("[" + contentType + "]");
    }
    process.write("\n");
    process.end();
  });
  req.end();

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
