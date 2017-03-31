package io.vertx.example.shell.echokeyboard

import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandRegistry
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.kotlin.ext.shell.*
import io.vertx.kotlin.ext.shell.term.*

class EchoKeyboardCommand : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var starwars = CommandBuilder.command("echokeyboard").processHandler({ process ->

      // Echo
      process.stdinHandler({ keys ->
        process.write(keys.replace('\r', '\n'))
      })

      // Terminate when user hits Ctrl-C
      process.interruptHandler({ v ->
        process.end()
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
