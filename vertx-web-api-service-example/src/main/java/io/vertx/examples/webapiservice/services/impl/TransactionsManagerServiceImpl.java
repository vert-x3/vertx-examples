package io.vertx.examples.webapiservice.services.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.examples.webapiservice.models.Transaction;
import io.vertx.examples.webapiservice.services.TransactionsManagerService;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionsManagerServiceImpl implements TransactionsManagerService {

  private TransactionPersistence persistence;

  public TransactionsManagerServiceImpl(TransactionPersistence persistence) {
    this.persistence = persistence;
  }

  @Override
  public void getTransactionsList(
    List<String> from,
    List<String> to,
    List<String> message,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
    List<Transaction> results = persistence.getFilteredTransactions(this.constructFilterPredicate(from, to, message));
    resultHandler.handle(Future.succeededFuture(
      OperationResponse.completedWithJson(
        new JsonArray(results.stream().map(Transaction::toJson).collect(Collectors.toList()))
      )
    ));
  }

  @Override
  public void createTransaction(
    Transaction body,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
    Transaction transactionAdded = persistence.addTransaction(body);
    resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(transactionAdded.toJson())));
  }

  @Override
  public void getTransaction(
    String transactionId,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
    Optional<Transaction> t = persistence.getTransaction(transactionId);
    if (t.isPresent())
      resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(t.get().toJson())));
    else
      resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
  }

  @Override
  public void updateTransaction(
    String transactionId,
    Transaction body,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
    if (persistence.updateTransaction(transactionId, body))
      resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithJson(body.toJson())));
    else
      resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
  }

  @Override
  public void deleteTransaction(
    String transactionId,
    OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler){
    if (persistence.removeTransaction(transactionId))
      resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("OK")));
    else
      resultHandler.handle(Future.succeededFuture(new OperationResponse().setStatusCode(404).setStatusMessage("Not Found")));
  }

  private Predicate<Transaction> constructFilterPredicate(List<String> from, List<String> to, List<String> message) {
    List<Predicate<Transaction>> predicates = new ArrayList<>();
    if (from != null) {
      predicates.add(transaction -> from.contains(transaction.getFrom()));
    }
    if (to != null) {
      predicates.add(transaction -> to.contains(transaction.getTo()));
    }
    if (message != null) {
      predicates.add(transaction -> message.stream().filter(o -> ((String)o).contains(transaction.getMessage())).count() > 0);
    }
    // Elegant predicates combination
    return predicates.stream().reduce(transaction -> true, Predicate::and);
  }

}
