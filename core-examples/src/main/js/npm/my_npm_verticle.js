/*
 * JavaScript Verticles can require NPM modules and deploy verticles packaged using the NPM _module_ format.
 * This verticle follows the NPM _module_ format, requires an other NPM (lib) and deploys a verticles provided as NPM.
 */
module.exports = {
    vertxStartAsync : function(startFuture) {
        // Require a NPM:
        console.log("Requiring npm");
        var hello = require = require("lib_as_npm/lib");
        hello("vertx");

        // Deploy another verticle following the NPM module format
        console.log("Deploying another verticle packages as NPM");
        vertx.deployVerticle("my_other_npm/my_other_verticle.js", function(res, err) {
            if (err) {
                startFuture.fail();
            } else {
                console.log("Done !");
                startFuture.complete();
            }
        });
    }
};
