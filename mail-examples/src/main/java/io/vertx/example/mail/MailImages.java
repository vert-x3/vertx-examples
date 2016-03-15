package io.vertx.example.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.example.util.Runner;
import io.vertx.ext.mail.MailAttachment;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;

/**
 * send a mail with HTML and inline images
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailImages extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(MailImages.class);
  }

  @Override
  public void start() {

    MailClient mailClient = MailClient.createShared(vertx, new MailConfig());

    MailMessage email = new MailMessage()
        .setFrom("user@example.com (Sender)")
        .setTo("user@example.com (User Name)")
        .setBounceAddress("user@example.com (Bounce)")
        .setSubject("Test email")
        .setText("full message is readable as html only")
        .setHtml("visit vert.x <a href=\"\"><img src=\"cid:image1@example.com\"></a>");

    List<MailAttachment> list=new ArrayList<>();
    MailAttachment attachment = new MailAttachment();
    attachment.setData(vertx.fileSystem().readFileBlocking("logo-white-big.png"));
    attachment.setContentType("image/png");
    attachment.setName("logo-white-big.png");
    attachment.setDisposition("inline");
    attachment.setHeaders(new CaseInsensitiveHeaders().add("Content-ID", "<image1@example.com>"));
    list.add(attachment);
    email.setInlineAttachment(list);

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
