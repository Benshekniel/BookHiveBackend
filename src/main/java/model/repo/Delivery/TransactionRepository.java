package model.repo.Delivery;

import model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    List<Transaction> findByType(Transaction.TransactionType type);
    List<Transaction> findByPaymentStatus(Transaction.PaymentStatus paymentStatus);
    List<Transaction> findByBookId(Long bookId);
    List<Transaction> findByBorrowerId(Long borrowerId);

    // Get all transactions with book and user details
    @Query("SELECT t, b.title, u.name " +
            "FROM Transaction t " +
            "LEFT JOIN Book b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers u ON t.borrowerId = u.user_id " +
            "ORDER BY t.createdAt DESC")
    List<Object[]> findAllTransactionsWithDetails();

    // Get hub revenue data
    @Query("SELECT SUM(t.paymentAmount), COUNT(t), t.paymentStatus " +
            "FROM Transaction t " +
            "JOIN Delivery d ON t.transactionId = d.transactionId " +
            "JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId " +
            "GROUP BY t.paymentStatus")
    List<Object[]> findHubRevenueData(@Param("hubId") Long hubId);

    // Get transactions by hub
    @Query("SELECT t, b.title, u.name " +
            "FROM Transaction t " +
            "LEFT JOIN Book b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers u ON t.borrowerId = u.user_id " +
            "JOIN Delivery d ON t.transactionId = d.transactionId " +
            "JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId " +
            "ORDER BY t.createdAt DESC")
    List<Object[]> findTransactionsByHub(@Param("hubId") Long hubId);

    // Get revenue summary
    @Query("SELECT t.paymentStatus, SUM(t.paymentAmount), COUNT(t) " +
            "FROM Transaction t " +
            "GROUP BY t.paymentStatus")
    List<Object[]> getRevenueSummary();

    // Get transaction stats by status
    @Query("SELECT t.status, COUNT(t), SUM(t.paymentAmount) " +
            "FROM Transaction t " +
            "GROUP BY t.status")
    List<Object[]> getTransactionStatsByStatus();

    // Get transactions by date range
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findByDateRange(@Param("startDate") java.time.LocalDateTime startDate,
                                      @Param("endDate") java.time.LocalDateTime endDate);

    // Get revenue for a specific time period
    @Query("SELECT SUM(t.paymentAmount) " +
            "FROM Transaction t " +
            "WHERE t.paymentStatus = 'COMPLETED' " +
            "AND t.createdAt BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getRevenueForPeriod(@Param("startDate") java.time.LocalDateTime startDate,
                                             @Param("endDate") java.time.LocalDateTime endDate);
}