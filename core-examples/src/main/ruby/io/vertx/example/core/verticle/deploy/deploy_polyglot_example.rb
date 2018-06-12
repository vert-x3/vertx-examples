
puts "Main verticle has started, let's deploy A JS one..."

# Deploy a verticle and don't wait for it to start,
# the js verticle will use the noop2 npm module (which does nothing)
# will invoke it and print a message
$vertx.deploy_verticle("jsverticle.js")
