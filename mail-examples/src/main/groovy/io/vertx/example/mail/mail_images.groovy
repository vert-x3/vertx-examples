import io.vertx.groovy.ext.mail.MailClient
import io.vertx.groovy.core.MultiMap

def mailClient = MailClient.createShared(vertx, [:])

def email = [
  from:"user@example.com (Sender)",
  to:"user@example.com (User Name)",
  subject:"Test email",
  text:"full message is readable as html only",
  html:"visit vert.x <a href=\"http://vertx.io/\"><img src=\"cid:image1@example.com\"></a>"
]

def list = []
def attachment = [:]
attachment.data = vertx.fileSystem().readFileBlocking("logo-white-big.png")
attachment.contentType = "image/png"
attachment.name = "logo-white-big.png"
attachment.disposition = "inline"
def headers = MultiMap.caseInsensitiveMultiMap()
headers.add("Content-ID", "<image1@example.com>")
attachment.headers = headers
list.add(attachment)
email.inlineAttachment = list

mailClient.sendMail(email, { result ->
  if (result.succeeded()) {
    println(result.result())
  } else {
    println("got exception")
    result.cause().printStackTrace()
  }
})
