package model.repo.Admin;


import model.entity.AllUsers;
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

    // Recent Activities - Users
    @Query(value = "SELECT u.user_id as id, 'user' as type, " +
            "CONCAT('New user registration: ', u.name) as message, " +
            "u.created_at as timestamp " +
            "FROM users u " +
            "WHERE u.created_at >= :since " +
            "ORDER BY u.created_at DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Map<String, Object>> getRecentUserActivities(@Param("since") LocalDateTime since);

    // Recent Activities - Books
    @Query(value = "SELECT b.book_id as id, 'book' as type,\n" +
            "       CASE\n" +
            "           WHEN b.status = 'AVAILABLE' THEN CONCAT('Book listing approved: \\\"', b.title, '\\\" by ', COALESCE((\n" +
            "               SELECT STRING_AGG(author, ', ')\n" +
            "               FROM jsonb_array_elements_text(b.authors) AS author\n" +
            "           ), 'Unknown Author'))\n" +
            "           ELSE CONCAT('Book listing rejected: \\\"', b.title, '\\\"')\n" +
            "       END as message,\n" +
            "       b.updated_at as timestamp\n" +
            "FROM books b\n" +
            "WHERE b.updated_at >= :since\n" +
            "GROUP BY b.book_id, b.title, b.status, b.updated_at\n" +
            "ORDER BY b.updated_at DESC\n" +
            "LIMIT 3;", nativeQuery = true)
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

    // Trust Score Calculation (mock implementation - adjust based on your trust score logic)
    @Query("SELECT AVG(CASE WHEN u.status = 'active' THEN 5.0 WHEN u.status = 'pending' THEN 3.0 ELSE 2.0 END) FROM AllUsers u")
    Double getAverageTrustScore();

    // Additional utility methods for dashboard
    @Query("SELECT COUNT(u) FROM AllUsers u WHERE u.status = 'banned'")
    Long getBannedUsers();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 'IN_TRANSIT'")
    Long getActiveDeliveries();
}
