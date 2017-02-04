require 'vertx/cli'
require 'vertx-shell/command_builder'
require 'vertx-shell/shell_service'
require 'vertx-shell/command_registry'

# Create the wget CLI
cli = Vertx::CLI.create("wget").set_summary("Wget implemented with Vert.x HTTP client").add_argument({
  'index' => 0,
  'argName' => "http-url",
  'description' => "the HTTP uri to get"
})

# Create the command
helloWorld = VertxShell::CommandBuilder.command(cli).process_handler() { |process|

  begin
    url = Java::JavaNet::URL.new(process.command_line().get_argument_value(0))
  rescue
    process.write("Bad url\n").end()
    return
  end

  client = process.vertx().create_http_client()
  process.write("Connecting to #{url}\n")
  port = url.get_port()
  if (port == -1)
    port = 80
  end
  req = client.get(port, url.get_host(), url.get_path())
  req.exception_handler() { |err|
    process.write("wget: error #{err.get_message()}\n")
    process.end()
  }
  req.handler() { |resp|
    process.write("#{resp.status_code()} #{resp.status_message()}\n")
    contentType = resp.get_header("Content-Type")
    contentLength = resp.get_header("Content-Length")
    process.write("Length: #{(contentLength != nil ? contentLength : "unspecified")}")
    if (contentType != nil)
      process.write("[#{contentType}]")
    end
    process.write("\n")
    process.end()
  }
  req.end()

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
