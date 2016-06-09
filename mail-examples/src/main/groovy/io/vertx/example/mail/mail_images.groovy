import io.vertx.groovy.ext.mail.MailClient
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
io.vertx.example.mail.LocalSmtpServer.start(2526)

def mailClient = MailClient.createShared(vertx, [
  port:2526
])

def email = [
  from:"user@example.com (Sender)",
  to:"user@example.com (User Name)",
  subject:"Test email",
  text:"full message is readable as html only",
  html:"visit vert.x <a href=\"http://vertx.io/\"><img src=\"cid:image1@example.com\"></a>"
]

def attachment = [
  data:vertx.fileSystem().readFileBlocking("logo-white-big.png"),
  contentType:"image/png",
  name:"logo-white-big.png",
  disposition:"inline",
  headers:[
    'Content-ID':"<image1@example.com>"
  ]
]

def list = []
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
