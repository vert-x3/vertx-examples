import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.command.CommandRegistry

def starwars = CommandBuilder.command("echokeyboard").processHandler({ process ->

  // Echo
  process.stdinHandler({ keys ->
    process.write(keys.replace('\r' as char, '\n' as char))
  })

  // Terminate when user hits Ctrl-C
  process.interruptHandler({ v ->
    process.end()
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
