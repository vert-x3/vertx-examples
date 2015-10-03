package io.vertx.example.shell.echokeyboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.net.TelnetOptions;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EchoKeyboardCommand extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(EchoKeyboardCommand.class);
  }

  @Override
  public void start() throws Exception {

    Command starwars = Command.builder("echokeyboard").
        processHandler(process -> {

          // Echo
          process.setStdin(keys -> {
            process.write(keys.replace('\r', '\n'));
          });

          // Terminate when user hits Ctrl-C
          process.eventHandler(EventType.SIGINT, v -> {
            process.end();
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
