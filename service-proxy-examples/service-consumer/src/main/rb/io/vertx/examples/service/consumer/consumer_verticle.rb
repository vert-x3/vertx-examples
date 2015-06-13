require 'json'
require 'vertx-processor-sample/processor_service'
service = VertxProcessorSample::ProcessorService.create_proxy($vertx, "vertx.processor")

document = {
  'name' => "vertx"
}

service.process(document) { |r,r_err|
  if (r_err == nil)
    puts JSON.generate(r)
  else
    puts r_err
  end
}
