package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailService;

/**
 * send a mail via event bus to the mail service running on another machine
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailServiceExample extends AbstractVerticle {

  public static final String MAIL_SERVICE_VERTICLE = "io.vertx.ext.mail.MailServiceVerticle";

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(MailServiceExample.class);
  }


  public void start() {
    // Start a local STMP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    LocalSmtpServer.start(2527);

    JsonObject config = new JsonObject();
    config.put("port", 2527);
    config.put("address", "vertx.mail");
    vertx.deployVerticle(MAIL_SERVICE_VERTICLE, new DeploymentOptions().setConfig(config), done -> {
      MailService mailService = MailService.createEventBusProxy(vertx, "vertx.mail");

      MailMessage email = new MailMessage()
          .setBounceAddress("bounce@example.com")
          .setTo("user@example.com")
          .setSubject("this message has no content at all");

      mailService.sendMail(email, result -> {
        if (result.succeeded()) {
          System.out.println(result.result());
          System.out.println("Mail sent");
        } else {
          System.out.println("got exception");
          result.cause().printStackTrace();
        }
      });

    });


  }

}
