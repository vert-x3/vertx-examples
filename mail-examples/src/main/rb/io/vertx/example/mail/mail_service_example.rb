require 'vertx-mail/mail_service'
@MAIL_SERVICE_VERTICLE = "io.vertx.ext.mail.MailServiceVerticle"
# Start a local STMP server, remove this line if you want to use your own server.
# It just prints the sent message to the console
Java::IoVertxExampleMail::LocalSmtpServer.start(2527)

config = {
}
config['port'] = 2527
config['address'] = "vertx.mail"
$vertx.deploy_verticle(@MAIL_SERVICE_VERTICLE, {
  'config' => config
}) { |done_err,done|
  mailService = VertxMail::MailService.create_event_bus_proxy($vertx, "vertx.mail")

  email = {
    'bounceAddress' => "bounce@example.com",
    'to' => "user@example.com",
    'subject' => "this message has no content at all"
  }

  mailService.send_mail(email) { |result_err,result|
    if (result_err == nil)
      puts result
    else
      puts "got exception"
      result_err.print_stack_trace()
    end
  }

}


