package io.vertx.example.mail;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.mail.*;
import io.vertx.launcher.application.VertxApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * send a mail via a smtp server requiring TLS and Login we use an attachment and a text/html alternative mail body
 * <p>
 * Please put in your actual mail server and account to run this example
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class MailLogin extends VerticleBase {

  public static void main(String[] args) {
    // Start a local SMTP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    LocalSmtpServer.startWithAuth(5870);

    VertxApplication.main(new String[]{MailLogin.class.getName()});
  }

  private MailClient mailClient;

  public Future<?> start() {
    // Start a local SMTP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    LocalSmtpServer.startWithAuth(5870);

    MailConfig mailConfig = new MailConfig()
      .setHostname("localhost")
      .setPort(5870)
      //.setStarttls(StartTLSOptions.REQUIRED)
      .setLogin(LoginOption.REQUIRED)
      .setAuthMethods("PLAIN")
      .setUsername("username")
      .setPassword("password");

    mailClient = MailClient.createShared(vertx, mailConfig);

    Buffer image = vertx.fileSystem().readFileBlocking("io/vertx/example/mail/logo-white-big.png");

    MailMessage email = new MailMessage()
      .setFrom("user1@example.com")
      .setTo("user2@example.com")
      .setCc("user3@example.com")
      .setBcc("user4@example.com")
      .setBounceAddress("bounce@example.com")
      .setSubject("Test email with HTML")
      .setText("this is a message")
      .setHtml("<a href=\"http://vertx.io\">vertx.io</a>");

    List<MailAttachment> list = new ArrayList<>();

    list.add(MailAttachment.create()
      .setData(image)
      .setName("logo-white-big.png")
      .setContentType("image/png")
      .setDisposition("inline")
      .setDescription("logo of vert.x web page"));

    email.setAttachment(list);

    return mailClient
      .sendMail(email)
      .onSuccess(result -> {
        System.out.println(result);
        System.out.println("Mail sent");
    });
  }
}
