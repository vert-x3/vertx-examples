package io.vertx.example.mail;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.launcher.application.VertxApplication;

import java.util.Arrays;

/**
 * send a mail with default config via localhost:25
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailLocalhost extends VerticleBase {

  public static void main(String[] args) {
    // Start a local SMTP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    LocalSmtpServer.start(2525);

    VertxApplication.main(new String[]{MailLocalhost.class.getName()});
  }

  private MailClient mailClient;

  @Override
  public Future<?> start() {
    mailClient = MailClient.createShared(vertx, new MailConfig().setPort(2525));

    MailMessage email = new MailMessage()
        .setFrom("user@example.com (Sender)")
        .setTo(Arrays.asList(
            "user@example.com (User Name)",
            "other@example.com (Another User)"))
        .setBounceAddress("user@example.com (Bounce)")
        .setSubject("Test email")
        .setText("this is a test email");

    return mailClient
      .sendMail(email)
      .onSuccess(result -> {
        System.out.println(result);
        System.out.println("Mail sent");
      });
  }

}
