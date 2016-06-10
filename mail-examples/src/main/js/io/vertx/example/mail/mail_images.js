var MailClient = require("vertx-mail-js/mail_client");
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
Java.type("io.vertx.example.mail.LocalSmtpServer").start(2526);

var mailClient = MailClient.createShared(vertx, {
  "port" : 2526
});

var email = {
  "from" : "user@example.com (Sender)",
  "to" : "user@example.com (User Name)",
  "subject" : "Test email",
  "text" : "full message is readable as html only",
  "html" : "visit vert.x <a href=\"http://vertx.io/\"><img src=\"cid:image1@example.com\"></a>"
};

var attachment = {
  "data" : vertx.fileSystem().readFileBlocking("logo-white-big.png"),
  "contentType" : "image/png",
  "name" : "logo-white-big.png",
  "disposition" : "inline",
  "headers" : {
    "Content-ID" : "<image1@example.com>"
  }
};

var list = [];
list.push(attachment);
email.inlineAttachment = list;

mailClient.sendMail(email, function (result, result_err) {
  if (result_err == null) {
    console.log(result);
    console.log("Mail sent");
  } else {
    console.log("got exception");
    result_err.printStackTrace();
  }
});
