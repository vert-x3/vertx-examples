import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.ShellService

def helloWorld = CommandBuilder.command("hello-world").processHandler({ process ->
  process.write("hello world\n")
  process.end()
}).build()

def service = ShellService.create(vertx, [
  telnetOptions:[
    host:"localhost",
    port:3000
  ]
])
service.getCommandRegistry().registerCommand(helloWorld)
service.start({ ar ->
  if (!ar.succeeded()) {
    ar.cause().printStackTrace()
  }
})
