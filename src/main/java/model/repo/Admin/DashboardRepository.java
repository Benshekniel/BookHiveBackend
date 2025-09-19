package model.repo.Admin;

import model.entity.AllUsers;
import model.entity.UserBooks;
import model.entity.Transaction;
import model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface DashboardRepository extends JpaRepository<AllUsers, Integer> {

    // User Statistics
    @Query("SELECT COUNT(u) FROM AllUsers u")
    Long getTotalUsers();

    @Query("SELECT COUNT(u) FROM Users u WHERE u.createdAt >= :startDate")
    Long getUsersCreatedAfter(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(u) FROM AllUsers u WHERE u.status = 'pending'")
    Long getPendingUserApprovals();

    @Query("SELECT COUNT(u) FROM AllUsers u WHERE u.role = 'MODERATOR' AND u.status = 'active'")
    Long getActiveModerators();

    // Book Statistics
    @Query("SELECT COUNT(b) FROM UserBooks b WHERE b.status = 'AVAILABLE'")
    Long getActiveListings();

    @Query("SELECT COUNT(b) FROM UserBooks b WHERE b.createdAt >= :startDate AND b.status = 'AVAILABLE'")
    Long getActiveListingsCreatedAfter(@Param("startDate") LocalDateTime startDate);

    // Transaction Statistics
    @Query("SELECT COALESCE(SUM(t.paymentAmount), 0) FROM Transaction t WHERE t.createdAt >= :startDate AND t.paymentStatus = 'COMPLETED'")
    BigDecimal getMonthlyRevenue(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COALESCE(SUM(t.paymentAmount), 0) FROM Transaction t WHERE t.createdAt >= :startDate AND t.createdAt < :endDate AND t.paymentStatus = 'COMPLETED'")
    BigDecimal getRevenueForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Recent Activities - Users (Using JPQL instead of native query)
    @Query("SELECT u.userId as id, 'user' as type, " +
            "CONCAT('New user registration: ', u.name) as message, " +
            "u.createdAt as timestamp " +
            "FROM Users u " +
            "WHERE u.createdAt >= :since " +
            "ORDER BY u.createdAt DESC")
    List<Map<String, Object>> getRecentUserActivities(@Param("since") LocalDateTime since);

    // Recent Activities - Books (Using JPQL)
    @Query("SELECT b.bookId as id, 'book' as type, " +
            "CASE " +
            "WHEN b.status = 'AVAILABLE' THEN CONCAT('Book listing approved: \"', b.title, '\"') " +
            "ELSE CONCAT('Book listing rejected: \"', b.title, '\"') " +
            "END as message, " +
            "b.updatedAt as timestamp " +
            "FROM UserBooks b " +
            "WHERE b.updatedAt >= :since " +
            "ORDER BY b.updatedAt DESC")
    List<Map<String, Object>> getRecentBookActivities(@Param("since") LocalDateTime since);

    // Recent Activities - Transactions/Disputes
    @Query(value = "SELECT t.transaction_id as id, 'alert' as type, " +
            "CASE " +
            "WHEN t.status = 'OVERDUE' THEN 'Dispute reported: Book return issue' " +
            "WHEN t.status = 'CANCELLED' THEN 'Transaction cancelled' " +
            "ELSE 'Transaction completed' " +
            "END as message, " +
            "t.updated_at as timestamp " +
            "FROM transactions t " +
            "WHERE t.updated_at >= :since AND t.status IN ('OVERDUE', 'CANCELLED') " +
            "ORDER BY t.updated_at DESC " +
            "LIMIT 2", nativeQuery = true)
    List<Map<String, Object>> getRecentTransactionActivities(@Param("since") LocalDateTime since);

    // Quick Action Counts
    @Query("SELECT COUNT(b) FROM UserBooks b WHERE b.status = 'PENDING'")
    Long getReportedItems();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = 'OVERDUE'")
    Long getActiveDisputes();

    // Trust Score Calculation
    @Query("SELECT AVG(CASE WHEN u.status = 'active' THEN 5.0 WHEN u.status = 'pending' THEN 3.0 ELSE 2.0 END) FROM AllUsers u")
    Double getAverageTrustScore();

    // Additional utility methods for dashboard
    @Query("SELECT COUNT(u) FROM AllUsers u WHERE u.status = 'banned'")
    Long getBannedUsers();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 'IN_TRANSIT'")
    Long getActiveDeliveries();
}