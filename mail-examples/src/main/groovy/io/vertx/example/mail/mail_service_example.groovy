import groovy.transform.Field
import io.vertx.ext.mail.MailService
@Field def MAIL_SERVICE_VERTICLE = "io.vertx.ext.mail.MailServiceVerticle"
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
io.vertx.example.mail.LocalSmtpServer.start(2527)

def config = [:]
config.port = 2527
config.address = "vertx.mail"
vertx.deployVerticle(MAIL_SERVICE_VERTICLE, [
  config:config
], { done ->
  def mailService = MailService.createEventBusProxy(vertx, "vertx.mail")

  def email = [
    bounceAddress:"bounce@example.com",
    to:"user@example.com",
    subject:"this message has no content at all"
  ]

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


