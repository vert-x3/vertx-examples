require 'vertx-mail/mail_service'
mailService = VertxMail::MailService.create_event_bus_proxy($vertx, "vertx.mail")

email = {
  'bounceAddress' => "bounce@example.com",
  'to' => "user@example.com",
  'subject' => "this message has no content at all"
}

mailService.send_mail(email) { |result,result_err|
  if (result_err == nil)
    puts result
  else
    puts "got exception"
    result_err.print_stack_trace()
  end
}
