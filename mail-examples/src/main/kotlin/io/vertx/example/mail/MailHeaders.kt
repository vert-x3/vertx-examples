package io.vertx.example.mail

import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailConfig
import io.vertx.ext.mail.MailMessage
import io.vertx.kotlin.ext.mail.*

class MailHeaders : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    // Start a local STMP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    io.vertx.example.mail.LocalSmtpServer.start(2528)
    var mailConfig = MailConfig(
      hostname = "localhost",
      port = 2528)

    var mailClient = MailClient.createShared(vertx, mailConfig)

    var email = MailMessage(
      from = "user1@example.com",
      to = listOf("user2@example.com", "user3@example.com", "user4@example.com"),
      headers = mapOf(
        "X-Mailer" to "Vert.x Mail-Client 3.6.3",
        "Message-ID" to "12345@example.com",
        "Reply-To" to "reply@example.com",
        "Received" to listOf("by vertx mail service", "from [192.168.1.1] by localhost")
      ),
      text = "This message should have a custom Message-ID")

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
