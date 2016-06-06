var MailService = require("vertx-mail-js/mail_service");
var MAIL_SERVICE_VERTICLE = "io.vertx.ext.mail.MailServiceVerticle";
// Start a local STMP server, remove this line if you want to use your own server.
// It just prints the sent message to the console
Java.type("io.vertx.example.mail.LocalSmtpServer").start(2527);

var config = {
};
config.port = 2527;
config.address = "vertx.mail";
vertx.deployVerticle(MAIL_SERVICE_VERTICLE, {
  "config" : config
}, function (done, done_err) {
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

});


