package io.vertx.examples.webapiservice.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.examples.webapiservice.models.Transaction;
import io.vertx.examples.webapiservice.persistence.TransactionPersistence;
import io.vertx.examples.webapiservice.services.impl.TransactionsManagerServiceImpl;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;

import java.util.List;

/**
 * This interface describes the Transactions Manager Service. Note that all methods has same name of corresponding operation id
 *
 */
@WebApiServiceGen
public interface TransactionsManagerService {

  static TransactionsManagerService create(TransactionPersistence persistence) {
    return new TransactionsManagerServiceImpl(persistence);
  }

  void getTransactionsList(
    List<String> from,
    List<String> to,
    List<String> message,
    ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void createTransaction(
    Transaction body,
    ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void getTransaction(
    String transactionId,
    ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void updateTransaction(
    String transactionId,
    Transaction body,
    ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

  void deleteTransaction(
    String transactionId,
    ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

}
