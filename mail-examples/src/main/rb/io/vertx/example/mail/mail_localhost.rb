require 'vertx-mail/mail_client'

mailClient = VertxMail::MailClient.create($vertx, {
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
