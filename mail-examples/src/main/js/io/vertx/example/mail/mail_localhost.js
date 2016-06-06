var MailClient = require("vertx-mail-js/mail_client");
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
Java.type("io.vertx.example.mail.LocalSmtpServer").start(2525);

var mailClient = MailClient.createShared(vertx, {
  "port" : 2525
});

var email = {
  "from" : "user@example.com (Sender)",
  "to" : ["user@example.com (User Name)", "other@example.com (Another User)"],
  "bounceAddress" : "user@example.com (Bounce)",
  "subject" : "Test email",
  "text" : "this is a test email"
};

mailClient.sendMail(email, function (result, result_err) {
  if (result_err == null) {
    console.log(result);
  } else {
    console.log("got exception");
    result_err.printStackTrace();
  }
});
