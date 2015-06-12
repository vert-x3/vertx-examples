/**
 * A verticle formatted as a NPM module.
 */
module.exports = {
    vertxStart : function() {
        console.log("Starting my other NPM");
    },

    vertxStop : function() {
        console.log("Stopping my other NPM");
    }
};
