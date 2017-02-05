package io.vertx.example.mail

import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailConfig
import io.vertx.ext.mail.MailMessage
import io.vertx.kotlin.ext.mail.*

class MailLocalhost : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    // Start a local STMP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    io.vertx.example.mail.LocalSmtpServer.start(2525)

    var mailClient = MailClient.createShared(vertx, MailConfig(
      port = 2525))

    var email = MailMessage(
      from = "user@example.com (Sender)",
      to = listOf("user@example.com (User Name)", "other@example.com (Another User)"),
      bounceAddress = "user@example.com (Bounce)",
      subject = "Test email",
      text = "this is a test email")

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
