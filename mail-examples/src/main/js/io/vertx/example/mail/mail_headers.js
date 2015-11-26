var MailClient = require("vertx-mail-js/mail_client");
var MultiMap = require("vertx-js/multi_map");
var mailConfig = {
  "hostname" : "smtp.example.com",
  "port" : 465,
  "ssl" : true
};

var mailClient = MailClient.createShared(vertx, mailConfig);

var email = {
  "from" : "user1@example.com",
  "to" : ["user2@example.com", "user3@example.com", "user4@example.com"]
};

var headers = MultiMap.caseInsensitiveMultiMap();

headers.add("X-Mailer", "Vert.x Mail-Client 3.1");
headers.add("Message-ID", "12345@example.com");
headers.add("Reply-To", "reply@example.com");
headers.add("Received", "by vertx mail service");
headers.add("Received", "from [192.168.1.1] by localhost");

email.headers = headers;
email.text = "This message should have a custom Message-ID";

mailClient.sendMail(email, function (result, result_err) {
  console.log("mail is finished");
});
