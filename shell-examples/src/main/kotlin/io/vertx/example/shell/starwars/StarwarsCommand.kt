package io.vertx.example.shell.starwars

import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandRegistry
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.kotlin.ext.shell.*
import io.vertx.kotlin.ext.shell.term.*

class StarwarsCommand : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var starwars = CommandBuilder.command("starwars").processHandler({ process ->

      // Connect the client
      var client = process.vertx().createNetClient()
      client.connect(23, "towel.blinkenlights.nl", { ar ->
        if (ar.succeeded()) {
          var socket = ar.result()

          // Ctrl-C closes the socket
          process.interruptHandler({ v ->
            socket.close()
          })

          //
          socket.handler({ buff ->
            // Push the data to the Shell
            process.write(buff.toString("UTF-8"))
          }).exceptionHandler({ err ->
            err.printStackTrace()
            socket.close()
          })

          // When socket closes, end the command
          socket.closeHandler({ v ->
            process.end()
          })
        } else {
          process.write("Could not connect to remote Starwars server\n").end()
        }
      })
    }).build(vertx)

    var service = ShellService.create(vertx, ShellServiceOptions(
      telnetOptions = TelnetTermOptions(
        host = "localhost",
        port = 3000)))
    CommandRegistry.getShared(vertx).registerCommand(starwars)
    service.start({ ar ->
      if (!ar.succeeded()) {
        ar.cause().printStackTrace()
      }
    })
  }
}
