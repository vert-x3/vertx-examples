var MailClient = require("vertx-mail-js/mail_client");
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
Java.type("io.vertx.example.mail.LocalSmtpServer").startWithAuth(5870);


var mailConfig = {
  "hostname" : "localhost",
  "port" : 5870,
  "login" : 'REQUIRED',
  "authMethods" : "PLAIN",
  "username" : "username",
  "password" : "password"
};

var mailClient = MailClient.createShared(vertx, mailConfig);

var image = vertx.fileSystem().readFileBlocking("logo-white-big.png");

var email = {
  "from" : "user1@example.com",
  "to" : "user2@example.com",
  "cc" : "user3@example.com",
  "bcc" : "user4@example.com",
  "bounceAddress" : "bounce@example.com",
  "subject" : "Test email with HTML",
  "text" : "this is a message",
  "html" : "<a href=\"http://vertx.io\">vertx.io</a>"
};

var list = [];

list.push({
  "data" : image,
  "name" : "logo-white-big.png",
  "contentType" : "image/png",
  "disposition" : "inline",
  "description" : "logo of vert.x web page"
});

email.attachment = list;

mailClient.sendMail(email, function (result, result_err) {
  if (result_err == null) {
    console.log(result);
    console.log("Mail sent");
  } else {
    console.log("got exception");
    result_err.printStackTrace();
  }
});
