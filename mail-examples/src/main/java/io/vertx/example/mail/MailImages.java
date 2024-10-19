package io.vertx.example.mail;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.mail.MailAttachment;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.launcher.application.VertxApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * send a mail with HTML and inline images
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailImages extends VerticleBase {

  public static void main(String[] args) {
    // Start a local SMTP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    LocalSmtpServer.start(2526);

    VertxApplication.main(new String[]{MailImages.class.getName()});
  }

  private MailClient mailClient;

  @Override
  public Future<?> start() {
    mailClient = MailClient.createShared(vertx, new MailConfig().setPort(2526));

    MailMessage email = new MailMessage()
      .setFrom("user@example.com (Sender)")
      .setTo("user@example.com (User Name)")
      .setSubject("Test email")
      .setText("full message is readable as html only")
      .setHtml("visit vert.x <a href=\"http://vertx.io/\"><img src=\"cid:image1@example.com\"></a>");

    MailAttachment attachment = MailAttachment.create()
      .setData(vertx.fileSystem().readFileBlocking("io/vertx/example/mail/logo-white-big.png"))
      .setContentType("image/png")
      .setName("logo-white-big.png")
      .setDisposition("inline")
      .addHeader("Content-ID", "<image1@example.com>");

    List<MailAttachment> list = new ArrayList<>();
    list.add(attachment);
    email.setInlineAttachment(list);

    return mailClient
      .sendMail(email)
      .onSuccess(result -> {
      System.out.println(result);
      System.out.println("Mail sent");
    });
  }

}
