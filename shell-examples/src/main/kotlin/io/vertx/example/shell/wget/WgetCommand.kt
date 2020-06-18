package io.vertx.example.shell.wget

import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandRegistry
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.kotlin.core.cli.*
import io.vertx.kotlin.ext.shell.*
import io.vertx.kotlin.ext.shell.term.*

class WgetCommand : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    // Create the wget CLI
    var cli = CLI.create("wget").setSummary("Wget implemented with Vert.x HTTP client").addArgument(Argument(
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
      client.get(port, url.getHost(), url.getPath(), { ar ->
        if (ar.succeeded()) {
          var resp = ar.result()
          process.write("${resp.statusCode()} ${resp.statusMessage()}\n")
          var contentType = resp.getHeader("Content-Type")
          var contentLength = resp.getHeader("Content-Length")
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

    var service = ShellService.create(vertx, ShellServiceOptions(
      telnetOptions = TelnetTermOptions(
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
