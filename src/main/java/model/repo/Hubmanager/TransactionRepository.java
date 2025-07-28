package model.repo.Hubmanager;

import model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    List<Transaction> findByType(Transaction.TransactionType type);
    List<Transaction> findByPaymentStatus(Transaction.PaymentStatus paymentStatus);
}
