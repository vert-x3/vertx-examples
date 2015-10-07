require 'vertx-shell/command_builder'
require 'vertx-shell/shell_service'

starwars = VertxShell::CommandBuilder.command("echokeyboard").process_handler() { |process|

  # Echo
  process.set_stdin() { |keys|
    process.write(keys.replace('\r', '\n'))
  }

  # Terminate when user hits Ctrl-C
  process.event_handler(:SIGINT) { |v|
    process.end()
  }

}.build()

service = VertxShell::ShellService.create($vertx, {
  'telnetOptions' => {
    'host' => "localhost",
    'port' => 3000
  }
})
service.get_command_registry().register_command(starwars)
service.start() { |ar_err,ar|
  if (!ar_err == nil)
    ar_err.print_stack_trace()
  end
}
