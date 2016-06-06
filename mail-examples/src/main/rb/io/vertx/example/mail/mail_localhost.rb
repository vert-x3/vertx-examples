require 'vertx-mail/mail_client'
# Start a local STMP server, remove this line if you want to use your own server.
# It just prints the sent message to the console
Java::IoVertxExampleMail::LocalSmtpServer.start(2525)

mailClient = VertxMail::MailClient.create_shared($vertx, {
  'port' => 2525
})

email = {
  'from' => "user@example.com (Sender)",
  'to' => ["user@example.com (User Name)", "other@example.com (Another User)"],
  'bounceAddress' => "user@example.com (Bounce)",
  'subject' => "Test email",
  'text' => "this is a test email"
}

mailClient.send_mail(email) { |result_err,result|
  if (result_err == nil)
    puts result
  else
    puts "got exception"
    result_err.print_stack_trace()
  end
}
