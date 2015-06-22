import io.vertx.groovy.ext.mail.MailClient

def mailClient = MailClient.createShared(vertx, [:])

def email = [
  from:"user@example.com (Sender)",
  to:["user@example.com (User Name)", "other@example.com (Another User)"],
  bounceAddress:"user@example.com (Bounce)",
  subject:"Test email",
  text:"this is a test email"
]

mailClient.sendMail(email, { result ->
  if (result.succeeded()) {
    println(result.result())
  } else {
    println("got exception")
    result.cause().printStackTrace()
  }
})
