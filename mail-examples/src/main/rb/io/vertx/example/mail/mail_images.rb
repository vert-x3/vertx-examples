require 'vertx-mail/mail_client'
require 'vertx/multi_map'
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

list = Array.new
attachment = {
}
attachment['data'] = $vertx.file_system().read_file_blocking("logo-white-big.png")
attachment['contentType'] = "image/png"
attachment['name'] = "logo-white-big.png"
attachment['disposition'] = "inline"
headers = Vertx::MultiMap.case_insensitive_multi_map()
headers.add("Content-ID", "<image1@example.com>")
attachment['headers'] = headers
list.push(attachment)
email['inlineAttachment'] = list

mailClient.send_mail(email) { |result_err,result|
  if (result_err == nil)
    puts result
  else
    puts "got exception"
    result_err.print_stack_trace()
  end
}
