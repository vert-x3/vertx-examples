package io.vertx.example.shell.helloworld

import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandRegistry
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.ext.shell.*
import io.vertx.kotlin.ext.shell.term.*

class HelloWorldCommand : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var helloWorld = CommandBuilder.command("hello-world").processHandler({ process ->
      process.write("hello world\n")
      process.end()
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
