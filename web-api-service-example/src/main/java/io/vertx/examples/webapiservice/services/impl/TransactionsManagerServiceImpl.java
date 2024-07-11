package io.vertx.examples.webapiservice.services.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.examples.webapiservice.models.Transaction;
import io.vertx.examples.webapiservice.persistence.TransactionPersistence;
import io.vertx.examples.webapiservice.services.TransactionsManagerService;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionsManagerServiceImpl implements TransactionsManagerService {

  private final TransactionPersistence persistence;

  public TransactionsManagerServiceImpl(TransactionPersistence persistence) {
    this.persistence = persistence;
  }

  @Override
  public Future<ServiceResponse> getTransactionsList(
    List<String> from,
    List<String> to,
    List<String> message,
    ServiceRequest request) {
    List<Transaction> results = persistence.getFilteredTransactions(this.constructFilterPredicate(from, to, message));
    return Future.succeededFuture(
      ServiceResponse.completedWithJson(
        new JsonArray(results.stream().map(Transaction::toJson).collect(Collectors.toList()))
      )
    );
  }

  @Override
  public Future<ServiceResponse> createTransaction(
    Transaction body,
    ServiceRequest request) {
    Transaction transactionAdded = persistence.addTransaction(body);
    return Future.succeededFuture(ServiceResponse.completedWithJson(transactionAdded.toJson()));
  }

  @Override
  public Future<ServiceResponse> getTransaction(
    String transactionId,
    ServiceRequest request) {
    Optional<Transaction> t = persistence.getTransaction(transactionId);
    if (t.isPresent())
      return Future.succeededFuture(ServiceResponse.completedWithJson(t.get().toJson()));
    else
      return Future.succeededFuture(new ServiceResponse().setStatusCode(404).setStatusMessage("Not Found"));
  }

  @Override
  public Future<ServiceResponse> updateTransaction(
    String transactionId,
    Transaction body,
    ServiceRequest request) {
    if (persistence.updateTransaction(transactionId, body))
      return Future.succeededFuture(ServiceResponse.completedWithJson(body.toJson()));
    else
      return Future.succeededFuture(new ServiceResponse().setStatusCode(404).setStatusMessage("Not Found"));
  }

  @Override
  public Future<ServiceResponse> deleteTransaction(
    String transactionId,
    ServiceRequest request) {
    if (persistence.removeTransaction(transactionId))
      return Future.succeededFuture(new ServiceResponse().setStatusCode(404).setStatusMessage("OK"));
    else
      return Future.succeededFuture(new ServiceResponse().setStatusCode(404).setStatusMessage("Not Found"));
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
      predicates.add(transaction -> message.stream().anyMatch(s -> s.contains(transaction.getMessage())));
    }
    // Elegant predicates combination
    return predicates.stream().reduce(transaction -> true, Predicate::and);
  }

}
