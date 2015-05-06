
# Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

$vertx.create_http_client({
  'ssl' => true,
  'trustAll' => true
}).get_now(4443, "localhost", "/") { |resp|
  puts "Got response #{resp.status_code()}"
  resp.body_handler() { |body|
    puts "Got data #{body.to_string("ISO-8859-1")}"}
}
