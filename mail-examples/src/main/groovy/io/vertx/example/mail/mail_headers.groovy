import io.vertx.ext.mail.MailClient
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
io.vertx.example.mail.LocalSmtpServer.start(2528)
def mailConfig = [
  hostname:"localhost",
  port:2528
]

def mailClient = MailClient.createShared(vertx, mailConfig)

def email = [
  from:"user1@example.com",
  to:["user2@example.com", "user3@example.com", "user4@example.com"],
  headers:[
    'X-Mailer':"Vert.x Mail-Client 4.0.0.Beta2",
    'Message-ID':"12345@example.com",
    'Reply-To':"reply@example.com",
    Received:[
      "by vertx mail service",
      "from [192.168.1.1] by localhost"
    ]
  ],
  text:"This message should have a custom Message-ID"
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
