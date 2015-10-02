require 'vertx-shell/command'
require 'vertx-shell/shell_service'

command = VertxShell::Command.builder("my-command").process_handler() { |process|
  process.write("hello sir\n").end()
}.build()

service = VertxShell::ShellService.create($vertx, {
  'telnetOptions' => {
    'host' => "localhost",
    'port' => 3000
  }
})
service.get_command_registry().register_command(command)
service.start()
