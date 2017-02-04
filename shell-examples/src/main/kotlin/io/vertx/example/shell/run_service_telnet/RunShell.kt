package io.vertx.example.shell.run_service_telnet

import io.vertx.ext.shell.ShellService
import io.vertx.kotlin.common.json.*

class RunShell : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var service = ShellService.create(vertx, io.vertx.ext.shell.ShellServiceOptions(
      telnetOptions = io.vertx.ext.shell.term.TelnetTermOptions(
        host = "localhost",
        port = 3000)))
    service.start()
  }
}
