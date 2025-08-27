package model.repo.Delivery;

import model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Existing methods
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    List<Transaction> findByType(Transaction.TransactionType type);
    List<Transaction> findByPaymentStatus(Transaction.PaymentStatus paymentStatus);

    // New optimized methods for dashboard
    @Query("SELECT COALESCE(SUM(t.paymentAmount), 0) FROM Transaction t WHERE t.paymentStatus = 'COMPLETED'")
    BigDecimal calculateTotalCompletedRevenue();

    @Query("SELECT " +
            "COUNT(t) as totalTransactions, " +
            "COUNT(CASE WHEN t.paymentStatus = 'COMPLETED' THEN 1 END) as completedTransactions, " +
            "COUNT(CASE WHEN t.paymentStatus = 'PENDING' THEN 1 END) as pendingTransactions, " +
            "COUNT(CASE WHEN t.paymentStatus = 'FAILED' THEN 1 END) as failedTransactions, " +
            "COALESCE(SUM(CASE WHEN t.paymentStatus = 'COMPLETED' THEN t.paymentAmount ELSE 0 END), 0) as totalRevenue " +
            "FROM Transaction t")
    Object[] getTransactionSummaryStats();

    // Hub-related revenue queries
    @Query("SELECT " +
            "COALESCE(SUM(CASE WHEN t.paymentStatus = 'COMPLETED' THEN t.paymentAmount ELSE 0 END), 0), " +
            "COUNT(t), " +
            "t.paymentStatus " +
            "FROM Transaction t " +
            "INNER JOIN Delivery d ON t.transactionId = d.transactionId " +
            "INNER JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId " +
            "GROUP BY t.paymentStatus")
    List<Object[]> findHubRevenueData(@Param("hubId") Long hubId);

    @Query("SELECT t.paymentStatus, COALESCE(SUM(t.paymentAmount), 0), COUNT(t) " +
            "FROM Transaction t " +
            "GROUP BY t.paymentStatus")
    List<Object[]> getRevenueSummary();

    @Query("SELECT t.status, COUNT(t), COALESCE(SUM(t.paymentAmount), 0) " +
            "FROM Transaction t " +
            "GROUP BY t.status")
    List<Object[]> getTransactionStatsByStatus();

    // Find transactions by hub through delivery relationship
    @Query("SELECT t, b.title, u.name " +
            "FROM Transaction t " +
            "LEFT JOIN UserBooks b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers u ON t.borrowerId = u.user_id " +
            "INNER JOIN Delivery d ON t.transactionId = d.transactionId " +
            "INNER JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId")
    List<Object[]> findTransactionsByHub(@Param("hubId") Long hubId);

    @Query("SELECT t, b.title, u.name " +
            "FROM Transaction t " +
            "LEFT JOIN UserBooks b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers u ON t.borrowerId = u.user_id")
    List<Object[]> findAllTransactionsWithDetails();

    // Performance queries
    @Query("SELECT t FROM Transaction t WHERE t.transactionId IN :transactionIds")
    List<Transaction> findByTransactionIds(@Param("transactionIds") List<Long> transactionIds);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.paymentStatus = :paymentStatus")
    Long countByPaymentStatus(@Param("paymentStatus") Transaction.PaymentStatus paymentStatus);
}