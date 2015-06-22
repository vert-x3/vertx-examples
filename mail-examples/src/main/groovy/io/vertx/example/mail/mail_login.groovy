import io.vertx.ext.mail.LoginOption
import io.vertx.ext.mail.StartTLSOptions
import io.vertx.groovy.ext.mail.MailClient
def mailConfig = [
  hostname:"smtp.example.com",
  port:587,
  starttls:StartTLSOptions.REQUIRED,
  login:LoginOption.REQUIRED,
  authMethods:"PLAIN",
  username:"username",
  password:"password"
]

def mailClient = MailClient.createShared(vertx, mailConfig)

def image = vertx.fileSystem().readFileBlocking("logo-white-big.png")

def email = [
  from:"user1@example.com",
  to:"user2@example.com",
  cc:"user3@example.com",
  bcc:"user4@example.com",
  bounceAddress:"bounce@example.com",
  subject:"Test email with HTML",
  text:"this is a message",
  html:"<a href=\"http://vertx.io\">vertx.io</a>"
]

def list = []

list.add([
  data:image,
  name:"logo-white-big.png",
  contentType:"image/png",
  disposition:"inline",
  description:"logo of vert.x web page"
])

email.attachment = list

mailClient.sendMail(email, { result ->
  if (result.succeeded()) {
    println(result.result())
  } else {
    println("got exception")
    result.cause().printStackTrace()
  }
})
