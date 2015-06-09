var MailClient = require("vertx-mail-js/mail_client");

var mailClient = MailClient.create(vertx, {
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
