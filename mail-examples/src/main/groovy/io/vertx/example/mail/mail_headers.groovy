import io.vertx.groovy.ext.mail.MailClient
import io.vertx.groovy.core.MultiMap
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
  to:["user2@example.com", "user3@example.com", "user4@example.com"]
]

def headers = MultiMap.caseInsensitiveMultiMap()

headers.add("X-Mailer", "Vert.x Mail-Client 3.3.0-SNAPSHOT")
headers.add("Message-ID", "12345@example.com")
headers.add("Reply-To", "reply@example.com")
headers.add("Received", "by vertx mail service")
headers.add("Received", "from [192.168.1.1] by localhost")

email.headers = headers
email.text = "This message should have a custom Message-ID"

mailClient.sendMail(email, { result ->
  println("mail has been sent")
})
