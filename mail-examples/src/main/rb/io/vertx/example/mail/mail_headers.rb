require 'vertx-mail/mail_client'
# Start a local STMP server, remove this line if you want to use your own server.
# It just prints the sent message to the console
Java::IoVertxExampleMail::LocalSmtpServer.start(2528)
mailConfig = {
  'hostname' => "localhost",
  'port' => 2528
}

mailClient = VertxMail::MailClient.create_shared($vertx, mailConfig)

email = {
  'from' => "user1@example.com",
  'to' => ["user2@example.com", "user3@example.com", "user4@example.com"],
  'headers' => {
    'X-Mailer' => "Vert.x Mail-Client 3.3.3",
    'Message-ID' => "12345@example.com",
    'Reply-To' => "reply@example.com",
    'Received' => [
      "by vertx mail service",
      "from [192.168.1.1] by localhost"
    ]
  },
  'text' => "This message should have a custom Message-ID"
}

mailClient.send_mail(email) { |result_err,result|
  if (result_err == nil)
    puts result
    puts "Mail sent"
  else
    puts "got exception"
    result_err.print_stack_trace()
  end
}
