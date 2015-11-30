import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.ShellService
import io.vertx.groovy.ext.shell.command.CommandRegistry

def starwars = CommandBuilder.command("starwars").processHandler({ process ->

  // Connect the client
  def client = process.vertx().createNetClient()
  client.connect(23, "towel.blinkenlights.nl", { ar ->
    if (ar.succeeded()) {
      def socket = ar.result()

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

def service = ShellService.create(vertx, [
  telnetOptions:[
    host:"localhost",
    port:3000
  ]
])
CommandRegistry.getShared(vertx).registerCommand(starwars)
service.start({ ar ->
  if (!ar.succeeded()) {
    ar.cause().printStackTrace()
  }
})
