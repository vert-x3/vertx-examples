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

/** @module vertx-processor-sample-js/processor_service */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JProcessorService = Java.type('io.vertx.examples.service.ProcessorService');

/**
 The service interface.

 @class
*/
var ProcessorService = function(j_val) {

  var j_processorService = j_val;
  var that = this;

  /**

   @public
   @param document {Object} 
   @param resultHandler {function} 
   */
  this.process =  function(document, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_processorService["process(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(document), function(ar) {
        if (ar.succeeded()) {
          resultHandler(utils.convReturnJson(ar.result()), null);
        } else {
          resultHandler(null, ar.cause());
        }
      });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_processorService;
};

ProcessorService._jclass = utils.getJavaClass("io.vertx.examples.service.ProcessorService");
ProcessorService._jtype = {accept: function(obj) {
    return ProcessorService._jclass.isInstance(obj._jdel);
  },wrap: function(jdel) {
    var obj = Object.create(ProcessorService.prototype, {});
    ProcessorService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
ProcessorService._create = function(jdel) {var obj = Object.create(ProcessorService.prototype, {});
  ProcessorService.apply(obj, arguments);
  return obj;
}
/**

 @memberof module:vertx-processor-sample-js/processor_service
 @param vertx {Vertx} 
 @return {ProcessorService}
 */
ProcessorService.create =  function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(ProcessorService, JProcessorService["create(io.vertx.core.Vertx)"](vertx._jdel)) ;
  } else throw new TypeError('function invoked with invalid arguments');
};

/**

 @memberof module:vertx-processor-sample-js/processor_service
 @param vertx {Vertx} 
 @param address {string} 
 @return {ProcessorService}
 */
ProcessorService.createProxy =  function(vertx, address) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
    return utils.convReturnVertxGen(ProcessorService, JProcessorService["createProxy(io.vertx.core.Vertx,java.lang.String)"](vertx._jdel, address)) ;
  } else throw new TypeError('function invoked with invalid arguments');
};

ProcessorService.NO_NAME_ERROR = JProcessorService.NO_NAME_ERROR;
ProcessorService.BAD_NAME_ERROR = JProcessorService.BAD_NAME_ERROR;
module.exports = ProcessorService;