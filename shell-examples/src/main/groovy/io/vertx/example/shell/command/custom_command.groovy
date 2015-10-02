import io.vertx.groovy.ext.shell.command.Command
import io.vertx.groovy.ext.shell.ShellService

def command = Command.builder("my-command").processHandler({ process ->
  process.write("hello sir\n").end()
}).build()

def service = ShellService.create(vertx, [
  telnetOptions:[
    host:"localhost",
    port:3000
  ]
])
service.getCommandRegistry().registerCommand(command)
service.start()
