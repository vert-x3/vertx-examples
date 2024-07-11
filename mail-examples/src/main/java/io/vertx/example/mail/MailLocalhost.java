package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;

import java.util.Arrays;

/**
 * send a mail with default config via localhost:25
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailLocalhost extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", MailLocalhost.class.getName());
  }

  @Override
  public void start() {
    // Start a local SMTP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    LocalSmtpServer.start(2525);

    MailClient mailClient = MailClient.createShared(vertx, new MailConfig().setPort(2525));

    MailMessage email = new MailMessage()
        .setFrom("user@example.com (Sender)")
        .setTo(Arrays.asList(
            "user@example.com (User Name)",
            "other@example.com (Another User)"))
        .setBounceAddress("user@example.com (Bounce)")
        .setSubject("Test email")
        .setText("this is a test email");

    mailClient.sendMail(email).onComplete(result -> {
      if (result.succeeded()) {
        System.out.println(result.result());
        System.out.println("Mail sent");
      } else {
        System.out.println("got exception");
        result.cause().printStackTrace();
      }
    });
  }

}
