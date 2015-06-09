var MailClient = require("vertx-mail-js/mail_client");
var mailConfig = {
  "hostname" : "smtp.example.com",
  "port" : 587,
  "starttls" : 'REQUIRED',
  "login" : 'REQUIRED',
  "authMethods" : "PLAIN",
  "username" : "username",
  "password" : "password"
};

var mailClient = MailClient.create(vertx, mailConfig);

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
  } else {
    console.log("got exception");
    result_err.printStackTrace();
  }
});
