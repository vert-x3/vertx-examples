
puts "Main verticle has started, let's deploy some others..."

# Different ways of deploying verticles

# Deploy a verticle and don't wait for it to start
$vertx.deploy_verticle("io.vertx.example.core.verticle.deploy.OtherVerticle")

# Deploy another instance and  want for it to start
$vertx.deploy_verticle("io.vertx.example.core.verticle.deploy.OtherVerticle") { |res_err,res|
  if (res_err == nil)

    deploymentID = res

    puts "Other verticle deployed ok, deploymentID = #{deploymentID}"

    # You can also explicitly undeploy a verticle deployment.
    # Note that this is usually unnecessary as any verticles deployed by a verticle will be automatically
    # undeployed when the parent verticle is undeployed

    $vertx.undeploy(deploymentID) { |res2_err,res2|
      if (res2_err == nil)
        puts "Undeployed ok!"
      else
        res2_err.print_stack_trace()
      end
    }

  else
    res_err.print_stack_trace()
  end
}

# Deploy specifying some config
config = {
  'foo' => "bar"
}
$vertx.deploy_verticle("io.vertx.example.core.verticle.deploy.OtherVerticle", {
  'config' => config
})

# Deploy 10 instances
$vertx.deploy_verticle("io.vertx.example.core.verticle.deploy.OtherVerticle", {
  'instances' => 10
})

# Deploy it as a worker verticle
$vertx.deploy_verticle("io.vertx.example.core.verticle.deploy.OtherVerticle", {
  'worker' => true
})


