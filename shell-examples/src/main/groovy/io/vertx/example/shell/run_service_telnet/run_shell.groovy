import io.vertx.groovy.ext.shell.ShellService
def service = ShellService.create(vertx, [
  telnetOptions:[
    host:"localhost",
    port:3000
  ]
])
service.start()
