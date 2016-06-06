import io.vertx.groovy.ext.mail.MailClient
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
io.vertx.example.mail.LocalSmtpServer.start(2525)

def mailClient = MailClient.createShared(vertx, [
  port:2525
])

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
    println("Mail sent")
  } else {
    println("got exception")
    result.cause().printStackTrace()
  }
})
