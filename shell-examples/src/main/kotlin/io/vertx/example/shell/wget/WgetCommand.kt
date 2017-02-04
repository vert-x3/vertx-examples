package io.vertx.example.shell.wget

import io.vertx.core.cli.CLI
import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandRegistry
import io.vertx.kotlin.common.json.*

class WgetCommand : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Create the wget CLI
    var cli = CLI.create("wget").setSummary("Wget implemented with Vert.x HTTP client").addArgument(io.vertx.core.cli.Argument(
      index = 0,
      argName = "http-url",
      description = "the HTTP uri to get"))

    // Create the command
    var helloWorld = CommandBuilder.command(cli).processHandler({ process ->
      var url: java.net.URL
      try {
        url = java.net.URL(process.commandLine().getArgumentValue<Any>(0))
      } catch(e: Exception) {
        process.write("Bad url\n").end()
        return
      }

      var client = process.vertx().createHttpClient()
      process.write("Connecting to ${url}\n")
      var port = url.getPort()
      if (port == -1) {
        port = 80
      }
      var req = client.get(port, url.getHost(), url.getPath())
      req.exceptionHandler({ err ->
        process.write("wget: error ${err.getMessage()}\n")
        process.end()
      })
      req.handler({ resp ->
        process.write("${resp.statusCode()} ${resp.statusMessage()}\n")
        var contentType = resp.getHeader("Content-Type")
        var contentLength = resp.getHeader("Content-Length")
        process.write("Length: ${(contentLength != null ? contentLength : "unspecified")}")
        if (contentType != null) {
          process.write("[${contentType}]")
        }
        process.write("\n")
        process.end()
      })
      req.end()

    }).build(vertx)

    var service = ShellService.create(vertx, io.vertx.ext.shell.ShellServiceOptions(
      telnetOptions = io.vertx.ext.shell.term.TelnetTermOptions(
        host = "localhost",
        port = 3000)))
    CommandRegistry.getShared(vertx).registerCommand(helloWorld)
    service.start({ ar ->
      if (!ar.succeeded()) {
        ar.cause().printStackTrace()
      }
    })
  }
}
