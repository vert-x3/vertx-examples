/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/** @module vertx-sockjs-proxy-example-js/my_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JMyService = Java.type('io.vertx.example.web.proxies.MyService');

/**
 @class
*/
var MyService = function(j_val) {

  var j_myService = j_val;
  var that = this;

  var __super_sayHello = this.sayHello;
  /**

   @public
   @param name {string} 
   @param handler {function} 
   @return {MyService}
   */
  this.sayHello =  function(name, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_myService["sayHello(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
        if (ar.succeeded()) {
          handler(ar.result(), null);
        } else {
          handler(null, ar.cause());
        }
      }) ;
      return that;
    } else if (typeof __super_sayHello != 'undefined') {
      return __super_sayHello.apply(this, __args);
    }
    else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_myService;
};

MyService._jclass = utils.getJavaClass("io.vertx.example.web.proxies.MyService");
MyService._jtype = {accept: function(obj) {
    return MyService._jclass.isInstance(obj._jdel);
  },wrap: function(jdel) {
    var obj = Object.create(MyService.prototype, {});
    MyService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
MyService._create = function(jdel) {var obj = Object.create(MyService.prototype, {});
  MyService.apply(obj, arguments);
  return obj;
}
module.exports = MyService;