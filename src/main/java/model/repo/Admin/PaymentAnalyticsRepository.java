package model.repo.Admin;

import model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentAnalyticsRepository extends JpaRepository<Transaction, Long> {

    // Use a simpler approach without CAST operations
    @Query(value = "SELECT * FROM transactions t WHERE " +
            "(:searchTerm IS NULL OR :searchTerm = '' OR " +
            "t.transaction_id::TEXT LIKE '%' || :searchTerm || '%' OR " +
            "t.book_id::TEXT LIKE '%' || :searchTerm || '%' OR " +
            "t.user_id::TEXT LIKE '%' || :searchTerm || '%') AND " +
            "(:status IS NULL OR :status = '' OR t.status = :status) AND " +
            "(:type IS NULL OR :type = '' OR t.type = :type) AND " +
            "(:paymentStatus IS NULL OR :paymentStatus = '' OR t.payment_status = :paymentStatus) AND " +
            "(CAST(:startDate AS TIMESTAMP) IS NULL OR t.created_at >= CAST(:startDate AS TIMESTAMP)) AND " +
            "(CAST(:endDate AS TIMESTAMP) IS NULL OR t.created_at <= CAST(:endDate AS TIMESTAMP)) " +
            "ORDER BY t.created_at DESC",
            countQuery = "SELECT COUNT(*) FROM transactions t WHERE " +
                    "(:searchTerm IS NULL OR :searchTerm = '' OR " +
                    "t.transaction_id::TEXT LIKE '%' || :searchTerm || '%' OR " +
                    "t.book_id::TEXT LIKE '%' || :searchTerm || '%' OR " +
                    "t.user_id::TEXT LIKE '%' || :searchTerm || '%') AND " +
                    "(:status IS NULL OR :status = '' OR t.status = :status) AND " +
                    "(:type IS NULL OR :type = '' OR t.type = :type) AND " +
                    "(:paymentStatus IS NULL OR :paymentStatus = '' OR t.payment_status = :paymentStatus) AND " +
                    "(CAST(:startDate AS TIMESTAMP) IS NULL OR t.created_at >= CAST(:startDate AS TIMESTAMP)) AND " +
                    "(CAST(:endDate AS TIMESTAMP) IS NULL OR t.created_at <= CAST(:endDate AS TIMESTAMP))",
            nativeQuery = true)
    Page<Transaction> findTransactionsWithFiltersNative(
            @Param("searchTerm") String searchTerm,
            @Param("status") String status,
            @Param("type") String type,
            @Param("paymentStatus") String paymentStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query(value = "SELECT * FROM transactions t WHERE " +
            "(:searchTerm IS NULL OR :searchTerm = '' OR " +
            "t.transaction_id::TEXT LIKE '%' || :searchTerm || '%' OR " +
            "t.book_id::TEXT LIKE '%' || :searchTerm || '%' OR " +
            "t.user_id::TEXT LIKE '%' || :searchTerm || '%') AND " +
            "(:status IS NULL OR :status = '' OR t.status = :status) AND " +
            "(:type IS NULL OR :type = '' OR t.type = :type) AND " +
            "(:paymentStatus IS NULL OR :paymentStatus = '' OR t.payment_status = :paymentStatus) AND " +
            "(CAST(:startDate AS TIMESTAMP) IS NULL OR t.created_at >= CAST(:startDate AS TIMESTAMP)) AND " +
            "(CAST(:endDate AS TIMESTAMP) IS NULL OR t.created_at <= CAST(:endDate AS TIMESTAMP)) " +
            "ORDER BY t.created_at DESC",
            nativeQuery = true)
    List<Transaction> findAllTransactionsWithFiltersNative(
            @Param("searchTerm") String searchTerm,
            @Param("status") String status,
            @Param("type") String type,
            @Param("paymentStatus") String paymentStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Keep your existing methods as they are
    @Query("SELECT COALESCE(SUM(t.paymentAmount), 0) FROM Transaction t WHERE t.paymentStatus = :paymentStatus")
    BigDecimal getTotalAmountByPaymentStatus(@Param("paymentStatus") Transaction.PaymentStatus paymentStatus);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.paymentStatus = :paymentStatus")
    Long countByPaymentStatus(@Param("paymentStatus") Transaction.PaymentStatus paymentStatus);

    @Query("SELECT t FROM Transaction t WHERE t.paymentStatus = :paymentStatus ORDER BY t.createdAt DESC")
    List<Transaction> findByPaymentStatus(@Param("paymentStatus") Transaction.PaymentStatus paymentStatus);

    @Query("SELECT t FROM Transaction t WHERE t.status = :status AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByStatusAndCreatedAtBetween(
            @Param("status") Transaction.TransactionStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = :status")
    Long countByStatus(@Param("status") Transaction.TransactionStatus status);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.type = :type")
    Long countByType(@Param("type") Transaction.TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId ORDER BY t.createdAt DESC")
    List<Transaction> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.bookId = :bookId ORDER BY t.createdAt DESC")
    List<Transaction> findByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COALESCE(SUM(t.paymentAmount), 0) FROM Transaction t WHERE " +
            "t.paymentStatus = 'COMPLETED' AND " +
            "t.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Rest of your methods remain the same...
}