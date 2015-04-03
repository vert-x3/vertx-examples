package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailService;

import java.util.Arrays;

/**
 * send a mail via a smtp service using SSL
 * we use a pregenerated string (this could be done with e.g. commons-email or javamail)
 * and use the address information from the MailMessage object
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 *
 */
public class MailPregen extends AbstractVerticle {

  public void start() {
    MailConfig mailConfig = new MailConfig("smtp.example.com", 465).setSsl(true);

    MailService mailService = MailService.create(vertx, mailConfig);

    MailMessage email = new MailMessage()
      .setFrom("user1@example.com")
      .setTo(Arrays.asList("user2@example.com",
          "user3@example.com",
          "user4@example.com"));

    // note that the from and to fields in the message string are not evaluated at all
    // (in fact an empty string would be a valid message)

    String message = "MIME-Version: 1.0\n" + 
        "Date: Fri, 3 Apr 2015 01:44:18 +0200\n" + 
        "Message-ID: <CAOc7+6WZoDGRMTrGS6+qaN5wuUZmVrCkaea_Z0V3SUL-K7ebhQ@mail.example.com>\n" + 
        "Subject: testmail\n" + 
        "From: Alexander Lehmann <alexlehm@example.com>\n" + 
        "To: Alexander Lehmann <alexlehm@example.com>\n" + 
        "Content-Type: multipart/alternative; boundary=001a113dff8406b62e0512c6690d\n" + 
        "\n" + 
        "--001a113dff8406b62e0512c6690d\n" + 
        "Content-Type: text/plain; charset=UTF-8\n" + 
        "\n" + 
        "this is a test mail from vertx <http://vertx.io/>\n" + 
        "\n" + 
        "--001a113dff8406b62e0512c6690d\n" + 
        "Content-Type: text/html; charset=UTF-8\n" + 
        "\n" + 
        "<div dir=\"ltr\">this is a test mail from <a href=\"http://vertx.io/\">vertx</a><br><br><br></div>\n" + 
        "\n" + 
        "--001a113dff8406b62e0512c6690d--";

    mailService.sendMailString(email, message, result -> {
      System.out.println("mail is finished");
    });
  }

}
