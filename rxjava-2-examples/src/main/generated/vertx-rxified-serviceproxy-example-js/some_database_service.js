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

/** @module vertx-rxified-serviceproxy-example-js/some_database_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSomeDatabaseService = Java.type('io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService');

/**
 @class
*/
var SomeDatabaseService = function(j_val) {

  var j_someDatabaseService = j_val;
  var that = this;

  /**

   @public
   @param id {number} 
   @param resultHandler {function} 
   @return {SomeDatabaseService}
   */
  this.getDataById =  function(id, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_someDatabaseService["getDataById(int,io.vertx.core.Handler)"](id, function(ar) {
        if (ar.succeeded()) {
          resultHandler(utils.convReturnJson(ar.result()), null);
        } else {
          resultHandler(null, ar.cause());
        }
      }) ;
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_someDatabaseService;
};

SomeDatabaseService._jclass = utils.getJavaClass("io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService");
SomeDatabaseService._jtype = {accept: function(obj) {
    return SomeDatabaseService._jclass.isInstance(obj._jdel);
  },wrap: function(jdel) {
    var obj = Object.create(SomeDatabaseService.prototype, {});
    SomeDatabaseService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
SomeDatabaseService._create = function(jdel) {var obj = Object.create(SomeDatabaseService.prototype, {});
  SomeDatabaseService.apply(obj, arguments);
  return obj;
}
module.exports = SomeDatabaseService;