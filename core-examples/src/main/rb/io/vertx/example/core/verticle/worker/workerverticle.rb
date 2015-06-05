puts "[Worker] Starting in #{Java::JavaLang::Thread.current_thread().get_name()}"

$vertx.event_bus().consumer("sample.data") { |message|
  puts "[Worker] Consuming data in #{Java::JavaLang::Thread.current_thread().get_name()}"
  body = message.body().to_string()
  message.reply(body.to_upper_case())
}
