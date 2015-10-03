require 'vertx-shell/command'
require 'vertx-shell/shell_service'

starwars = VertxShell::Command.builder("starwars").process_handler() { |process|
  client = process.vertx().create_net_client()
  client.connect(23, "towel.blinkenlights.nl") { |ar_err,ar|
    if (ar_err == nil)
      socket = ar
      process.event_handler(:SIGINT) { |v|
        socket.close()
        process.end()
      }
      socket.handler() { |buff|
        process.write(buff.to_string("UTF-8"))
      }.exception_handler() { |err|
        err.print_stack_trace()
        process.end()
      }.end_handler() { |v|
        process.end()
      }
    else
      process.write("Could not connect to remote Starwars server\n").end()
    end
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
