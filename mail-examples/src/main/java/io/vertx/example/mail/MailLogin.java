package io.vertx.example.mail;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.mail.LoginOption;
import io.vertx.ext.mail.MailAttachment;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailService;
import io.vertx.ext.mail.StarttlsOption;

import java.util.ArrayList;
import java.util.List;

/**
 * send a mail via a smtp service requiring TLS and Login
 * we use an attachment and a text/html alternative mail body
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 *
 */
public class MailLogin extends AbstractVerticle {

  public void start() {
    MailConfig mailConfig = new MailConfig("smtp.example.com", 587, StarttlsOption.REQUIRED, LoginOption.REQUIRED)
      .setUsername("username")
      .setPassword("password");

    MailService mailService = MailService.create(vertx, mailConfig);

    Buffer image=vertx.fileSystem().readFileBlocking("logo-white-big.png");

    MailMessage email = new MailMessage()
      .setFrom("user1@example.com")
      .setTo("user2@example.com")
      .setCc("user3@example.com")
      .setBcc("user4@example.com")
      .setBounceAddress("bounce@example.com")
      .setSubject("Test email with HTML")
      .setText("this is a message")
      .setHtml("<a href=\"http://vertx.io\">vertx.io</a>");

    List<MailAttachment> list=new ArrayList<MailAttachment>();

    list.add(new MailAttachment()
      .setData(image)
      .setName("logo-white-big.png")
      .setContentType("image/png")
      .setDisposition("inline")
      .setDescription("logo of vert.x web page"));

    email.setAttachment(list);

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
