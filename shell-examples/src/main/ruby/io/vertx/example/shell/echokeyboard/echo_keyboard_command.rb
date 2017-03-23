require 'vertx-shell/command_builder'
require 'vertx-shell/shell_service'
require 'vertx-shell/command_registry'

starwars = VertxShell::CommandBuilder.command("echokeyboard").process_handler() { |process|

  # Echo
  process.stdin_handler() { |keys|
    process.write(keys.replace('\r', '\n'))
  }

  # Terminate when user hits Ctrl-C
  process.interrupt_handler() { |v|
    process.end()
  }

}.build($vertx)

service = VertxShell::ShellService.create($vertx, {
  'telnetOptions' => {
    'host' => "localhost",
    'port' => 3000
  }
})
VertxShell::CommandRegistry.get_shared($vertx).register_command(starwars)
service.start() { |ar_err,ar|
  if (!ar_err == nil)
    ar_err.print_stack_trace()
  end
}
