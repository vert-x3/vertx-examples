require 'vertx-shell/command_builder'
require 'vertx-shell/shell_service'

helloWorld = VertxShell::CommandBuilder.command("hello-world").process_handler() { |process|
  process.write("hello world\n")
  process.end()
}.build()

service = VertxShell::ShellService.create($vertx, {
  'telnetOptions' => {
    'host' => "localhost",
    'port' => 3000
  }
})
service.get_command_registry().register_command(helloWorld)
service.start() { |ar_err,ar|
  if (!ar_err == nil)
    ar_err.print_stack_trace()
  end
}
