package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailClient;

import java.util.Arrays;

/**
 * send a mail with default config via localhost:25
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailLocalhost extends AbstractVerticle {

  @Override
  public void start() {

    MailClient mailClient = MailClient.create(vertx, new MailConfig());

    MailMessage email = new MailMessage()
      .setFrom("user@example.com (Sender)")
      .setTo(Arrays.asList(
        "user@example.com (User Name)",
        "other@example.com (Another User)"))
      .setBounceAddress("user@example.com (Bounce)")
      .setSubject("Test email")
      .setText("this is a test email");

    mailClient.sendMail(email, result -> {
      if (result.succeeded()) {
        System.out.println(result.result());
      } else {
        System.out.println("got exception");
        result.cause().printStackTrace();
      }
    });
  }

}
