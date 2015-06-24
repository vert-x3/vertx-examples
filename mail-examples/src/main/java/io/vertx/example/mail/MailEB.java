package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailService;

/**
 * send a mail via event bus to the mail service running on another machine
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 *
 */
public class MailEB extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(MailEB.class);
  }


  public void start() {
    MailService mailService = MailService.createEventBusProxy(vertx, "vertx.mail");

    MailMessage email = new MailMessage()
      .setBounceAddress("bounce@example.com")
      .setTo("user@example.com")
      .setSubject("this message has no content at all");

    mailService.sendMail(email, result -> {
      if (result.succeeded()) {
        System.out.println(result.result());
      } else {
        System.out.println("got exception");
        result.cause().printStackTrace();
      }
    });
  }

}
