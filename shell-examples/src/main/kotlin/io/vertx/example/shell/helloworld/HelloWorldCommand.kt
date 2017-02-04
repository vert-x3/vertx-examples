package io.vertx.example.shell.helloworld

import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandRegistry
import io.vertx.kotlin.common.json.*

class HelloWorldCommand : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var helloWorld = CommandBuilder.command("hello-world").processHandler({ process ->
      process.write("hello world\n")
      process.end()
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
