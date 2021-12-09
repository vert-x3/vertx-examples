package io.vertx.example.shell.prompt;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.shell.ShellServer;
import io.vertx.ext.shell.command.CommandResolver;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.TermServer;

import java.util.concurrent.atomic.AtomicInteger;

public class PromptCommand  extends AbstractVerticle {
  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(PromptCommand.class);
  }

  @Override
  public void start() throws Exception {

    ShellServer server = ShellServer.create(vertx);
    AtomicInteger ai = new AtomicInteger(0);
    server.shellHandler(shell -> {
      shell.setPrompt(s -> {
        try {
          return "C: " + ai.incrementAndGet();
        } catch (Exception e) {
          System.err.println("Counter");
          e.printStackTrace();
        }
        return "NOOP";
      });
    });
    TelnetTermOptions tto = new TelnetTermOptions().setPort(3000).setHost("localhost");
    TermServer telnetTermServer = TermServer.createTelnetTermServer(vertx,tto);
    server.registerTermServer(telnetTermServer);

    server.registerCommandResolver(CommandResolver.baseCommands(vertx));

    server.listen(ar -> {
      if (!ar.succeeded()) {
        ar.cause().printStackTrace();
      }
    });


  }
}
