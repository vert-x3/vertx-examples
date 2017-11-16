require 'vertx/util/utils.rb'
# Generated from io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService
module VertxRxifiedServiceproxyExample
  class SomeDatabaseService
    # @private
    # @param j_del [::VertxRxifiedServiceproxyExample::SomeDatabaseService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxRxifiedServiceproxyExample::SomeDatabaseService] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == SomeDatabaseService
    end
    def @@j_api_type.wrap(obj)
      SomeDatabaseService.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExampleReactivexServicesServiceproxy::SomeDatabaseService.java_class
    end
    # @param [Fixnum] id 
    # @yield 
    # @return [self]
    def get_data_by_id(id=nil)
      if id.class == Fixnum && block_given?
        @j_del.java_method(:getDataById, [Java::int.java_class,Java::IoVertxCore::Handler.java_class]).call(id,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result != nil ? JSON.parse(ar.result.encode) : nil : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling get_data_by_id(#{id})"
    end
  end
end
