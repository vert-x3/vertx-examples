require 'vertx-mail/mail_client'
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

headers = Java::IoVertxCoreHttp::CaseInsensitiveHeaders.new()

headers.add("X-Mailer", "Vert.x Mail-Client 3.0")
headers.add("Message-ID", "12345@example.com")
headers.add("Reply-To", "reply@example.com")
headers.add("Received", "by vertx mail service")
headers.add("Received", "from [192.168.1.1] by localhost")

email['headers'] = headers
email['text'] = "This message should have a custom Message-ID"

mailClient.send_mail(email) { |result,result_err|
  puts "mail is finished"
}
