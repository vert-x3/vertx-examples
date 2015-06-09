import io.vertx.groovy.ext.mail.MailService
def mailService = MailService.createEventBusProxy(vertx, "vertx.mail")

def email = [
  bounceAddress:"bounce@example.com",
  to:"user@example.com",
  subject:"this message has no content at all"
]

mailService.sendMail(email, { result ->
  if (result.succeeded()) {
    println(result.result())
  } else {
    println("got exception")
    result.cause().printStackTrace()
  }
})
