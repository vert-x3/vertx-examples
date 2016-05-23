var MailClient = require("vertx-mail-js/mail_client");
var MultiMap = require("vertx-js/multi_map");

var mailClient = MailClient.createShared(vertx, {
});

var email = {
  "from" : "user@example.com (Sender)",
  "to" : "user@example.com (User Name)",
  "subject" : "Test email",
  "text" : "full message is readable as html only",
  "html" : "visit vert.x <a href=\"http://vertx.io/\"><img src=\"cid:image1@example.com\"></a>"
};

var list = [];
var attachment = {
};
attachment.data = vertx.fileSystem().readFileBlocking("../../../../../../../logo-white-big.png");
attachment.contentType = "image/png";
attachment.name = "logo-white-big.png";
attachment.disposition = "inline";
var headers = MultiMap.caseInsensitiveMultiMap();
headers.add("Content-ID", "<image1@example.com>");
attachment.headers = headers;
list.push(attachment);
email.inlineAttachment = list;

mailClient.sendMail(email, function (result, result_err) {
  if (result_err == null) {
    console.log(result);
  } else {
    console.log("got exception");
    result_err.printStackTrace();
  }
});
