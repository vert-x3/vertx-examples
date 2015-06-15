require 'vertx-mail/mail_client'
require 'vertx/multi_map'
mailConfig = {
  'hostname' => "smtp.example.com",
  'port' => 465,
  'ssl' => true
}

mailClient = VertxMail::MailClient.create($vertx, mailConfig)

email = {
  'from' => "user1@example.com",
  'to' => ["user2@example.com", "user3@example.com", "user4@example.com"]
}

headers = Vertx::MultiMap.case_insensitive_multi_map()

headers.add("X-Mailer", "Vert.x Mail-Client 3.0")
headers.add("Message-ID", "12345@example.com")
headers.add("Reply-To", "reply@example.com")
headers.add("Received", "by vertx mail service")
headers.add("Received", "from [192.168.1.1] by localhost")

email['headers'] = headers
email['text'] = "This message should have a custom Message-ID"

mailClient.send_mail(email) { |result_err,result|
  puts "mail is finished"
}
