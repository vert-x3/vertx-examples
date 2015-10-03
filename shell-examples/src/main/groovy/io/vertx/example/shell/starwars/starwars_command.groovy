import io.vertx.ext.shell.io.EventType
import io.vertx.groovy.ext.shell.command.Command
import io.vertx.groovy.ext.shell.ShellService

def starwars = Command.builder("starwars").processHandler({ process ->
  def client = process.vertx().createNetClient()
  client.connect(23, "towel.blinkenlights.nl", { ar ->
    if (ar.succeeded()) {
      def socket = ar.result()
      process.eventHandler(EventType.SIGINT, { v ->
        socket.close()
        process.end()
      })
      socket.handler({ buff ->
        process.write(buff.toString("UTF-8"))
      }).exceptionHandler({ err ->
        err.printStackTrace()
        process.end()
      }).endHandler({ v ->
        process.end()
      })
    } else {
      process.write("Could not connect to remote Starwars server\n").end()
    }
  })
}).build()

def service = ShellService.create(vertx, [
  telnetOptions:[
    host:"localhost",
    port:3000
  ]
])
service.getCommandRegistry().registerCommand(starwars)
service.start({ ar ->
  if (!ar.succeeded()) {
    ar.cause().printStackTrace()
  }
})
