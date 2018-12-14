package io.vertx.examples.webapiservice.persistence;

import io.vertx.examples.webapiservice.models.Transaction;
import io.vertx.examples.webapiservice.persistence.impl.TransactionPersistenceImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This interface represents a persistence layer of your application
 *
 * @author slinkydeveloper
 */
public interface TransactionPersistence {

  /**
   * Factory method to instantiate TransactionPersistence
   *
   * @return
   */
  static TransactionPersistence create() {
    return new TransactionPersistenceImpl();
  }

  List<Transaction> getFilteredTransactions(Predicate<Transaction> p);

  Optional<Transaction> getTransaction(String transactionId);

  Transaction addTransaction(Transaction t);

  boolean removeTransaction(String transactionId);

  boolean updateTransaction(String transactionId, Transaction transaction);
}
