package io.vertx.example.mail

import io.vertx.ext.mail.LoginOption
import io.vertx.ext.mail.MailAttachment
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailConfig
import io.vertx.ext.mail.MailMessage
import io.vertx.kotlin.ext.mail.*

class MailLogin : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    // Start a local STMP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    io.vertx.example.mail.LocalSmtpServer.startWithAuth(5870)


    var mailConfig = MailConfig(
      hostname = "localhost",
      port = 5870,
      login = LoginOption.REQUIRED,
      authMethods = "PLAIN",
      username = "username",
      password = "password")

    var mailClient = MailClient.createShared(vertx, mailConfig)

    var image = vertx.fileSystem().readFileBlocking("logo-white-big.png")

    var email = MailMessage(
      from = "user1@example.com",
      to = "user2@example.com",
      cc = "user3@example.com",
      bcc = "user4@example.com",
      bounceAddress = "bounce@example.com",
      subject = "Test email with HTML",
      text = "this is a message",
      html = "<a href=\"http://vertx.io\">vertx.io</a>")

    var list = mutableListOf<Any?>()

    list.add(MailAttachment(
      data = image,
      name = "logo-white-big.png",
      contentType = "image/png",
      disposition = "inline",
      description = "logo of vert.x web page"))

    email.attachment = list

    mailClient.sendMail(email, { result ->
      if (result.succeeded()) {
        println(result.result())
        println("Mail sent")
      } else {
        println("got exception")
        result.cause().printStackTrace()
      }
    })
  }
}
