
options = {
  'ssl' => true,
  'trustAll' => true
}

$vertx.create_net_client(options).connect(1234, "localhost") { |res_err,res|
  if (res_err == nil)
    sock = res
    sock.handler() { |buff|
      puts "client receiving #{buff.to_string("UTF-8")}"
    }

    # Now send some data
    i = 0
    while (i < 10)
      str = "hello #{i}\n"
      puts "Net client sending: #{str}"
      sock.write(str)
      i+=1
    end
  else
    puts "Failed to connect #{res_err}"
  end
}
