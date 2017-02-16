package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailClient;

import java.util.Arrays;

/**
 * send a mail via a smtp service with a few headers to the mail (this can be used for example for mail api
 * headers e.g. from mailgun or sendgrid or to add Reply-To and custom Received headers)
 * <p>
 * you can either supply all headers (setFixedHeaders(true)) or give a set of headers to be set over the headers that
 * are set for the mime message (Message-ID, From, To etc)
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailHeaders extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(MailHeaders.class);
  }


  public void start() {
    // Start a local STMP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    LocalSmtpServer.start(2528);
    MailConfig mailConfig = new MailConfig().setHostname("localhost").setPort(2528);

    MailClient mailClient = MailClient.createShared(vertx, mailConfig);

    MailMessage email = new MailMessage()
      .setFrom("user1@example.com")
      .setTo(Arrays.asList("user2@example.com", "user3@example.com", "user4@example.com"))
      .addHeader("X-Mailer", "Vert.x Mail-Client 3.4.0-SNAPSHOT")
      .addHeader("Message-ID", "12345@example.com")
      .addHeader("Reply-To", "reply@example.com")
      .addHeader("Received", "by vertx mail service")
      .addHeader("Received", "from [192.168.1.1] by localhost")
      .setText("This message should have a custom Message-ID");

    mailClient.sendMail(email, result -> {
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
