package io.vertx.example.shell.starwars;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.example.util.Runner;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.net.TelnetOptions;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StarwarsCommand extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(StarwarsCommand.class);
  }

  @Override
  public void start() throws Exception {

    Command starwars = Command.builder("starwars").
        processHandler(process -> {

          // Connect the client
          NetClient client = process.vertx().createNetClient();
          client.connect(23, "towel.blinkenlights.nl", ar -> {
            if (ar.succeeded()) {
              NetSocket socket = ar.result();

              // Ctrl-C closes the socket
              process.eventHandler(EventType.SIGINT, v -> {
                socket.close();
              });

              //
              socket.handler(buff -> {
                // Push the data to the Shell
                process.write(buff.toString("UTF-8"));
              }).exceptionHandler(err -> {
                err.printStackTrace();
                socket.close();
              });

              // When socket closes, end the command
              socket.closeHandler(v -> {
                process.end();
              });
            } else {
              process.write("Could not connect to remote Starwars server\n").end();
            }
          });
        }).build();

    ShellService service = ShellService.create(vertx, new ShellServiceOptions().setTelnetOptions(
        new TelnetOptions().setHost("localhost").setPort(3000)
    ));
    service.getCommandRegistry().registerCommand(starwars);
    service.start(ar -> {
      if (!ar.succeeded()) {
        ar.cause().printStackTrace();
      }
    });
  }
}
