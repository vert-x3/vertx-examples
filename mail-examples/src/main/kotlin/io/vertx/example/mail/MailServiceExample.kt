package io.vertx.example.mail

import io.vertx.core.DeploymentOptions
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailService
import io.vertx.kotlin.common.json.*
import io.vertx.kotlin.core.*
import io.vertx.kotlin.ext.mail.*

class MailServiceExample : io.vertx.core.AbstractVerticle()  {
  var MAIL_SERVICE_VERTICLE = "io.vertx.ext.mail.MailServiceVerticle"
  override fun start() {
    // Start a local STMP server, remove this line if you want to use your own server.
    // It just prints the sent message to the console
    io.vertx.example.mail.LocalSmtpServer.start(2527)

    var config = json {
      obj()
    }
    config.put("port", 2527)
    config.put("address", "vertx.mail")
    vertx.deployVerticle(MAIL_SERVICE_VERTICLE, DeploymentOptions(
      config = config), { done ->
      var mailService = MailService.createEventBusProxy(vertx, "vertx.mail")

      var email = MailMessage(
        bounceAddress = "bounce@example.com",
        to = "user@example.com",
        subject = "this message has no content at all")

      mailService.sendMail(email, { result ->
        if (result.succeeded()) {
          println(result.result())
          println("Mail sent")
        } else {
          println("got exception")
          result.cause().printStackTrace()
        }
      })

    })


  }
}
