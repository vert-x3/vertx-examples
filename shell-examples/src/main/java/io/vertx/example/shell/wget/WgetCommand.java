package io.vertx.example.shell.wget;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.example.util.Runner;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.net.TelnetOptions;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class WgetCommand extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(WgetCommand.class);
  }

  @Override
  public void start() throws Exception {

    CLI cli = CLI.create("wget").setSummary("Wget implemented with Vert.x HTTP client").
        addArgument(new Argument().setIndex(0).setArgName("http-url").setDescription("the HTTP uri to get"));
    Command helloWorld = Command.builder(cli).
        processHandler(process -> {
          URL url;
          try {
            url = new URL(process.commandLine().getArgumentValue(0));
          } catch (MalformedURLException e) {
            process.write("Bad url\n").end();
            return;
          }
          HttpClient client = process.vertx().createHttpClient();
          process.write("Connecting to " + url + "\n");
          int port = url.getPort();
          if (port == -1) {
            port = 80;
          }
          HttpClientRequest req = client.get(port, url.getHost(), url.getPath());
          req.exceptionHandler(err -> {
            process.write("wget: error " + err.getMessage() + "\n");
            process.end();
          });
          req.handler(resp -> {
            process.write(resp.statusCode() + " " + resp.statusMessage() + "\n");
            String contentType = resp.getHeader("Content-Type");
            String contentLength = resp.getHeader("Content-Length");
            process.write("Length: " + (contentLength != null ? contentLength : "unspecified"));
            if (contentType != null) {
              process.write("[" + contentType + "]");
            }
            process.write("\n");
            process.end();
          });
          req.end();

        }).build();

    ShellService service = ShellService.create(vertx, new ShellServiceOptions().setTelnetOptions(
        new TelnetOptions().setHost("localhost").setPort(3000)
    ));
    service.getCommandRegistry().registerCommand(helloWorld);
    service.start();
  }
}
