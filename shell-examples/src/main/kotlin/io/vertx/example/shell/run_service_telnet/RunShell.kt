package io.vertx.example.shell.run_service_telnet

import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.ext.shell.*
import io.vertx.kotlin.ext.shell.term.*

class RunShell : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var service = ShellService.create(vertx, ShellServiceOptions(
      telnetOptions = TelnetTermOptions(
        host = "localhost",
        port = 3000)))
    service.start()
  }
}
