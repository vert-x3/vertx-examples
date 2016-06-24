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
    Java::IoVertxExamplesServiceConsumer::Failures.deal_with_failure(r_err)
  end
}
