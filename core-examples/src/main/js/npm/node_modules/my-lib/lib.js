/**
 * A very simple library provided as NPM.
 * Just exposed a `sayHello` function.
 */
module.exports = function sayHello(name) {
    java.lang.System.out.println("Hello " + name);
};
