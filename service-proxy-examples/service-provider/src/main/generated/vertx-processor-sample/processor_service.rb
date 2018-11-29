require 'vertx/vertx'
require 'vertx/util/utils.rb'
# Generated from io.vertx.examples.service.ProcessorService
module VertxProcessorSample
  #  The service interface.
  class ProcessorService
    # @private
    # @param j_del [::VertxProcessorSample::ProcessorService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxProcessorSample::ProcessorService] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == ProcessorService
    end
    def @@j_api_type.wrap(obj)
      ProcessorService.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExamplesService::ProcessorService.java_class
    end
    # @param [::Vertx::Vertx] vertx 
    # @return [::VertxProcessorSample::ProcessorService]
    def self.create(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExamplesService::ProcessorService.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxProcessorSample::ProcessorService)
      end
      raise ArgumentError, "Invalid arguments when calling create(#{vertx})"
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [String] address 
    # @return [::VertxProcessorSample::ProcessorService]
    def self.create_proxy(vertx=nil,address=nil)
      if vertx.class.method_defined?(:j_del) && address.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExamplesService::ProcessorService.java_method(:createProxy, [Java::IoVertxCore::Vertx.java_class,Java::java.lang.String.java_class]).call(vertx.j_del,address),::VertxProcessorSample::ProcessorService)
      end
      raise ArgumentError, "Invalid arguments when calling create_proxy(#{vertx},#{address})"
    end
    # @param [Hash{String => Object}] document 
    # @yield 
    # @return [void]
    def process(document=nil)
      if document.class == Hash && block_given?
        return @j_del.java_method(:process, [Java::IoVertxCoreJson::JsonObject.java_class,Java::IoVertxCore::Handler.java_class]).call(::Vertx::Util::Utils.to_json_object(document),(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.encode) : nil : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling process(#{document})"
    end
    def self.NO_NAME_ERROR
      Java::IoVertxExamplesService::ProcessorService.NO_NAME_ERROR
    end
    def self.BAD_NAME_ERROR
      Java::IoVertxExamplesService::ProcessorService.BAD_NAME_ERROR
    end
  end
end
