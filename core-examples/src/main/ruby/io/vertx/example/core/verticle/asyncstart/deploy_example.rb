
puts "Main verticle has started, let's deploy some others..."

# Deploy another instance and  want for it to start
$vertx.deploy_verticle("io.vertx.example.core.verticle.asyncstart.OtherVerticle") { |res_err,res|
  if (res_err == nil)

    deploymentID = res

    puts "Other verticle deployed ok, deploymentID = #{deploymentID}"

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


