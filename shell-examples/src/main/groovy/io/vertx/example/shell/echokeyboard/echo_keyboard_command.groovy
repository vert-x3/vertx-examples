import io.vertx.ext.shell.io.EventType
import io.vertx.groovy.ext.shell.command.Command
import io.vertx.groovy.ext.shell.ShellService

def starwars = Command.builder("echokeyboard").processHandler({ process ->

  // Echo
  process.setStdin({ keys ->
    process.write(keys.replace('\r', '\n'))
  })

  // Terminate when user hits Ctrl-C
  process.eventHandler(EventType.SIGINT, { v ->
    process.end()
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
