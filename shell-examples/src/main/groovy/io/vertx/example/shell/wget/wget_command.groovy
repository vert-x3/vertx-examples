import io.vertx.core.cli.CLI
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.command.CommandRegistry

// Create the wget CLI
def cli = CLI.create("wget").setSummary("Wget implemented with Vert.x HTTP client").addArgument([
  index:0,
  argName:"http-url",
  description:"the HTTP uri to get"
])

// Create the command
def helloWorld = CommandBuilder.command(cli).processHandler({ process ->
  def url
  try {
    url = new java.net.URL(process.commandLine().getArgumentValue(0))
  } catch(Exception e) {
    process.write("Bad url\n").end()
    return
  }

  def client = process.vertx().createHttpClient()
  process.write("Connecting to ${url}\n")
  def port = url.getPort()
  if (port == -1) {
    port = 80
  }
  client.get(port, url.getHost(), url.getPath(), { ar ->
    if (ar.succeeded()) {
      def resp = ar.result()
      process.write("${resp.statusCode()} ${resp.statusMessage()}\n")
      def contentType = resp.getHeader("Content-Type")
      def contentLength = resp.getHeader("Content-Length")
      process.write("Length: ${(contentLength != null ? contentLength : "unspecified")}")
      if (contentType != null) {
        process.write("[${contentType}]")
      }
      process.write("\n")
      process.end()
    } else {
      process.write("wget: error ${ar.cause().getMessage()}\n")
      process.end()
    }
  })

}).build(vertx)

def service = ShellService.create(vertx, [
  telnetOptions:[
    host:"localhost",
    port:3000
  ]
])
CommandRegistry.getShared(vertx).registerCommand(helloWorld)
service.start({ ar ->
  if (!ar.succeeded()) {
    ar.cause().printStackTrace()
  }
})
