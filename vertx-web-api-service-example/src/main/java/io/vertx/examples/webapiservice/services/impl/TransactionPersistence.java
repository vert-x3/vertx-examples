package io.vertx.examples.webapiservice.services.impl;

import io.vertx.examples.webapiservice.models.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
public class TransactionPersistence {

    private Map<String, Transaction> transactions;

    public TransactionPersistence() {
        transactions = new HashMap<>();
    }

    public List<Transaction> getFilteredTransactions(Predicate<Transaction> p) {
        return transactions.values().stream().filter(p).collect(Collectors.toList());
    }

    public Optional<Transaction> getTransaction(String transactionId) {
      return Optional.ofNullable(transactions.get(transactionId));
    }

    public Transaction addTransaction(Transaction t) {
      transactions.put(t.getId(), t);
      return t;
    }

    public boolean removeTransaction(String transactionId) {
      Transaction t = transactions.remove(transactionId);
      if (t != null) return true;
      else return false;
    }

    public boolean updateTransaction(String transactionId, Transaction transaction) {
      Transaction t = transactions.replace(transactionId, transaction);
      if (t != null) return true;
      else return false;
    }
}
