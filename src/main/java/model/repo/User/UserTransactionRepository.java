package model.repo.User;

import model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserTransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ Base query without filters - SAFE
    @Query("SELECT t FROM Transaction t WHERE " +
            "t.userId = :userId OR t.borrowerId = :userId OR " +
            "t.sellerId = :userId OR t.lenderId = :userId OR t.exchangerId = :userId " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserInvolved(@Param("userId") Long userId, Pageable pageable);

    // ✅ Query with status filter only
    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) AND t.status = :status " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserAndStatus(@Param("userId") Long userId,
                                          @Param("status") Transaction.TransactionStatus status,
                                          Pageable pageable);

    // ✅ Query with type filter only
    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) AND t.type = :type " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserAndType(@Param("userId") Long userId,
                                        @Param("type") Transaction.TransactionType type,
                                        Pageable pageable);

    // ✅ Query with payment status filter only
    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) AND t.paymentStatus = :paymentStatus " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserAndPaymentStatus(@Param("userId") Long userId,
                                                 @Param("paymentStatus") Transaction.PaymentStatus paymentStatus,
                                                 Pageable pageable);

    // ✅ Query with date range filter only
    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) " +
            "AND t.createdAt >= :fromDate AND t.createdAt <= :toDate " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserAndDateRange(@Param("userId") Long userId,
                                             @Param("fromDate") LocalDateTime fromDate,
                                             @Param("toDate") LocalDateTime toDate,
                                             Pageable pageable);

    // ✅ Query with status and type
    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) " +
            "AND t.status = :status AND t.type = :type " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserAndStatusAndType(@Param("userId") Long userId,
                                                 @Param("status") Transaction.TransactionStatus status,
                                                 @Param("type") Transaction.TransactionType type,
                                                 Pageable pageable);

    // ✅ Native SQL fallback with proper casting - SAFE
    @Query(value = "SELECT * FROM transactions t WHERE " +
            "(t.user_id = :userId OR t.borrower_id = :userId OR t.seller_id = :userId OR " +
            "t.lender_id = :userId OR t.exchanger_id = :userId) " +
            "ORDER BY t.created_at DESC",
            nativeQuery = true)
    Page<Transaction> findByUserInvolvedNative(@Param("userId") Long userId, Pageable pageable);

    // All other existing methods remain the same...
    Optional<Transaction> findByTrackingNumber(@Param("trackingNumber") String trackingNumber);

    @Query("SELECT t FROM Transaction t WHERE " +
            "t.status = 'ACTIVE' AND t.endDate < :currentDate AND " +
            "(t.type = 'LOAN' OR t.type = 'AUCTION') " +
            "ORDER BY t.endDate ASC")
    List<Transaction> findOverdueTransactions(@Param("currentDate") LocalDateTime currentDate);

    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status, t.updatedAt = :updatedAt " +
            "WHERE t.transactionId = :transactionId")
    void updateStatus(@Param("transactionId") Long transactionId,
                      @Param("status") Transaction.TransactionStatus status,
                      @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Query("UPDATE Transaction t SET t.paymentStatus = :paymentStatus, t.updatedAt = :updatedAt " +
            "WHERE t.transactionId = :transactionId")
    void updatePaymentStatus(@Param("transactionId") Long transactionId,
                             @Param("paymentStatus") Transaction.PaymentStatus paymentStatus,
                             @Param("updatedAt") LocalDateTime updatedAt);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) AND t.status = :status")
    Long countByUserAndStatus(@Param("userId") Long userId,
                              @Param("status") Transaction.TransactionStatus status);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) AND t.type = :type")
    Long countByUserAndType(@Param("userId") Long userId,
                            @Param("type") Transaction.TransactionType type);

    @Query("SELECT " +
            "COUNT(t) as totalCount, " +
            "SUM(CASE WHEN t.status = 'ACTIVE' THEN 1 ELSE 0 END) as activeCount, " +
            "SUM(CASE WHEN t.status = 'COMPLETED' THEN 1 ELSE 0 END) as completedCount, " +
            "SUM(CASE WHEN t.status = 'OVERDUE' THEN 1 ELSE 0 END) as overdueCount, " +
            "SUM(CASE WHEN t.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelledCount, " +
            "SUM(CASE WHEN t.type = 'LOAN' THEN 1 ELSE 0 END) as borrowedCount, " +
            "SUM(CASE WHEN t.type = 'SALE' THEN 1 ELSE 0 END) as purchasedCount, " +
            "SUM(CASE WHEN t.type = 'AUCTION' THEN 1 ELSE 0 END) as auctionCount " +
            "FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId)")
    Object[] getUserTransactionSummary(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE " +
            "t.borrowerId = :userId AND t.type = 'LOAN' AND t.status = 'ACTIVE' " +
            "ORDER BY t.endDate ASC")
    List<Transaction> findActiveBorrowedBooks(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.userId = :userId OR t.borrowerId = :userId OR t.sellerId = :userId OR " +
            "t.lenderId = :userId OR t.exchangerId = :userId) " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findRecentTransactions(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Transaction t WHERE " +
            "t.transactionId = :transactionId AND " +
            "(t.userId = :userId OR t.borrowerId = :userId) AND " +
            "t.status IN ('PENDING', 'ACTIVE')")
    boolean canUserCancelTransaction(@Param("transactionId") Long transactionId,
                                     @Param("userId") Long userId);
}