require 'vertx-consul/consul_client'
consulClient = VertxConsul::ConsulClient.create($vertx)
consulClient.put_value("key11", "value11") { |putResult_err,putResult|
  if (putResult_err == nil)
    puts "KV pair saved"
    consulClient.get_value("key11") { |getResult_err,getResult|
      if (getResult_err == nil)
        puts "KV pair retrieved"
        puts getResult['value']
      else
        getResult_err.print_stack_trace()
      end
    }
  else
    putResult_err.print_stack_trace()
  end
}
