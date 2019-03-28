var MailClient = require("vertx-mail-js/mail_client");
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
Java.type("io.vertx.example.mail.LocalSmtpServer").start(2528);
var mailConfig = {
  "hostname" : "localhost",
  "port" : 2528
};

var mailClient = MailClient.createShared(vertx, mailConfig);

var email = {
  "from" : "user1@example.com",
  "to" : ["user2@example.com", "user3@example.com", "user4@example.com"],
  "headers" : {
    "X-Mailer" : "Vert.x Mail-Client 3.7.0",
    "Message-ID" : "12345@example.com",
    "Reply-To" : "reply@example.com",
    "Received" : [
      "by vertx mail service",
      "from [192.168.1.1] by localhost"
    ]
  },
  "text" : "This message should have a custom Message-ID"
};

mailClient.sendMail(email, function (result, result_err) {
  if (result_err == null) {
    console.log(result);
    console.log("Mail sent");
  } else {
    console.log("got exception");
    result_err.printStackTrace();
  }
});
