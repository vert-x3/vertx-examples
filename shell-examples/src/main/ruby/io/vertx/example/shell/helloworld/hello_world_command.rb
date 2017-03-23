require 'vertx-shell/command_builder'
require 'vertx-shell/shell_service'
require 'vertx-shell/command_registry'

helloWorld = VertxShell::CommandBuilder.command("hello-world").process_handler() { |process|
  process.write("hello world\n")
  process.end()
}.build($vertx)

service = VertxShell::ShellService.create($vertx, {
  'telnetOptions' => {
    'host' => "localhost",
    'port' => 3000
  }
})
VertxShell::CommandRegistry.get_shared($vertx).register_command(helloWorld)
service.start() { |ar_err,ar|
  if (!ar_err == nil)
    ar_err.print_stack_trace()
  end
}
