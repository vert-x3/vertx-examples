require 'json'
require 'vertx-processor-sample/processor_service'
service = VertxProcessorSample::ProcessorService.create_proxy($vertx, "vertx.processor")

document = {
  'name' => "vertx"
}

service.process(document) { |r_err,r|
  if (r_err == nil)
    puts JSON.generate(r)
  else
    if (r_err.class.name == 'Java::IoVertxServiceproxy::ServiceException')
      exc = r_err
      if (exc.failure_code() == $BAD_NAME_ERROR)
        puts "Failed to process the document: The name in the document is bad. The name provided is: #{exc.get_debug_info()['name']}"
      elsif (exc.failure_code() == $NO_NAME_ERROR)
        puts "Failed to process the document: No name was found"
      end
    else
      puts "Unexpected error: #{r_err}"

    end
  end
}
