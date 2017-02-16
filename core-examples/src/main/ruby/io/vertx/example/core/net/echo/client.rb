$vertx.create_net_client().connect(1234, "localhost") { |res_err,res|

  if (res_err == nil)
    socket = res
    socket.handler() { |buffer|
      puts "Net client receiving: #{buffer.to_string("UTF-8")}"
    }

    # Now send some data
    (0...10).each do |i|
      str = "hello #{i}\n"
      puts "Net client sending: #{str}"
      socket.write(str)
    end
  else
    puts "Failed to connect #{res_err}"
  end
}
