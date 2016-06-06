require 'vertx-mail/mail_client'
# Start a local STMP server, remove this line if you want to use your own server.
# It just prints the sent message to the console
Java::IoVertxExampleMail::LocalSmtpServer.start_with_auth(5870)


mailConfig = {
  'hostname' => "localhost",
  'port' => 5870,
  'login' => :REQUIRED,
  'authMethods' => "PLAIN",
  'username' => "username",
  'password' => "password"
}

mailClient = VertxMail::MailClient.create_shared($vertx, mailConfig)

image = $vertx.file_system().read_file_blocking("logo-white-big.png")

email = {
  'from' => "user1@example.com",
  'to' => "user2@example.com",
  'cc' => "user3@example.com",
  'bcc' => "user4@example.com",
  'bounceAddress' => "bounce@example.com",
  'subject' => "Test email with HTML",
  'text' => "this is a message",
  'html' => "<a href=\"http://vertx.io\">vertx.io</a>"
}

list = Array.new

list.push({
  'data' => image,
  'name' => "logo-white-big.png",
  'contentType' => "image/png",
  'disposition' => "inline",
  'description' => "logo of vert.x web page"
})

email['attachment'] = list

mailClient.send_mail(email) { |result_err,result|
  if (result_err == nil)
    puts result
    puts "Mail sent"
  else
    puts "got exception"
    result_err.print_stack_trace()
  end
}
