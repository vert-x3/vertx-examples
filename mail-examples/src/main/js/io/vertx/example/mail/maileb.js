var MailService = require("vertx-mail-js/mail_service");
var mailService = MailService.createEventBusProxy(vertx, "vertx.mail");

var email = {
  "bounceAddress" : "bounce@example.com",
  "to" : "user@example.com",
  "subject" : "this message has no content at all"
};

mailService.sendMail(email, function (result, result_err) {
  if (result_err == null) {
    console.log(result);
  } else {
    console.log("got exception");
    result_err.printStackTrace();
  }
});
