package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailService;

import java.util.Arrays;

/**
 * send a mail via a smtp service using SSL
 * we add a few headers to the mail (this can be used for example for mail api headers
 * e.g. from mailgun or sendgrid or to add Reply-To and custom Received headers)
 * <p>
 * you can either supply all headers (setFixedHeaders(true)) or give a set of headers to
 * be added to the headers that are set for the mime message (MIME-Version, From, To etc)
 *
 * TODO: setting headers that are added set in the regular mime headers (e.g. Message-ID)
 * doesn't work as of milestone5 unless you set setFixedHeaders(true)
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailHeaders extends AbstractVerticle {

  public void start() {
//    MailConfig mailConfig = new MailConfig("smtp.example.com", 465).setSsl(true);
    MailConfig mailConfig = new MailConfig();

    MailService mailService = MailService.create(vertx, mailConfig);

    MailMessage email = new MailMessage()
      .setFrom("user1@example.com")
      .setTo(Arrays.asList("user2@example.com",
          "user3@example.com",
          "user4@example.com"));

    MultiMap headers = new CaseInsensitiveHeaders();

    headers.add("X-Mailer", "vert.x mail-service 3");
    headers.add("Message-ID", "12345@example.com");
    headers.add("Reply-To", "reply@example.com");
    headers.add("Received", "by vertx mail service");
    headers.add("Received", "from [192.168.1.1] by localhost");

    email.setHeaders(headers);
    email.setText("This message should have a custom Message-ID");

    mailService.sendMail(email, result -> {
      System.out.println("mail is finished");
    });
  }

}
