require 'vertx/util/utils.rb'
# Generated from io.vertx.example.web.proxies.MyService
module VertxSockjsProxyExample
  class MyService
    # @private
    # @param j_del [::VertxSockjsProxyExample::MyService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxSockjsProxyExample::MyService] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == MyService
    end
    def @@j_api_type.wrap(obj)
      MyService.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExampleWebProxies::MyService.java_class
    end
    # @param [String] name 
    # @yield 
    # @return [self]
    def say_hello(name=nil)
      if name.class == String && block_given?
        @j_del.java_method(:sayHello, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(name,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling say_hello(#{name})"
    end
  end
end
