require 'vertx-mail/mail_client'
# Start a local STMP server, remove this line if you want to use your own server.
# It just prints the sent message to the console
Java::IoVertxExampleMail::LocalSmtpServer.start(2526)

mailClient = VertxMail::MailClient.create_shared($vertx, {
  'port' => 2526
})

email = {
  'from' => "user@example.com (Sender)",
  'to' => "user@example.com (User Name)",
  'subject' => "Test email",
  'text' => "full message is readable as html only",
  'html' => "visit vert.x <a href=\"http://vertx.io/\"><img src=\"cid:image1@example.com\"></a>"
}

attachment = {
  'data' => $vertx.file_system().read_file_blocking("logo-white-big.png"),
  'contentType' => "image/png",
  'name' => "logo-white-big.png",
  'disposition' => "inline",
  'headers' => {
    'Content-ID' => "<image1@example.com>"
  }
}

list = Array.new
list.push(attachment)
email['inlineAttachment'] = list

mailClient.send_mail(email) { |result_err,result|
  if (result_err == nil)
    puts result
    puts "Mail sent"
  else
    puts "got exception"
    result_err.print_stack_trace()
  end
}
