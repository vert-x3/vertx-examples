package io.vertx.example.mail

import io.vertx.ext.mail.MailAttachment
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailConfig
import io.vertx.ext.mail.MailMessage
import io.vertx.kotlin.ext.mail.*

class MailImages : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    // Start a local STMP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    io.vertx.example.mail.LocalSmtpServer.start(2526)

    var mailClient = MailClient.createShared(vertx, MailConfig(
      port = 2526))

    var email = MailMessage(
      from = "user@example.com (Sender)",
      to = "user@example.com (User Name)",
      subject = "Test email",
      text = "full message is readable as html only",
      html = "visit vert.x <a href=\"http://vertx.io/\"><img src=\"cid:image1@example.com\"></a>")

    var attachment = MailAttachment(
      data = vertx.fileSystem().readFileBlocking("logo-white-big.png"),
      contentType = "image/png",
      name = "logo-white-big.png",
      disposition = "inline",
      headers = mapOf("Content-ID" to "<image1@example.com>"))

    var list = mutableListOf<Any?>()
    list.add(attachment)
    email.inlineAttachment = list

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
