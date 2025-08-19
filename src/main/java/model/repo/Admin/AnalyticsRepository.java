package model.repo.Admin;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import model.entity.Users;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Users, Long> {

    // User Analytics Queries
    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    Long getTotalUsers();

    @Query(value = "SELECT COUNT(*) FROM users WHERE created_at BETWEEN ?1 AND ?2", nativeQuery = true)
    Long getUsersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT EXTRACT(YEAR FROM created_at) as year, COUNT(*) as user_count " +
            "FROM users " +
            "GROUP BY EXTRACT(YEAR FROM created_at) " +
            "ORDER BY year", nativeQuery = true)
    List<Object[]> getYearlyUserGrowth();

    // Book Analytics Queries
    @Query(value = "SELECT COUNT(*) FROM user_books WHERE for_sale = true OR for_lend = true OR for_exchange = true", nativeQuery = true)
    Long getActiveBooks();

    @Query(value = "SELECT COUNT(*) FROM user_books", nativeQuery = true)
    Long getTotalBooks();

    @Query(value = "SELECT COUNT(*) FROM user_books WHERE created_at BETWEEN ?1 AND ?2", nativeQuery = true)
    Long getBooksByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT genre, COUNT(*) as count " +
            "FROM (SELECT jsonb_array_elements_text(genres) as genre FROM user_books WHERE genres IS NOT NULL) as genre_data " +
            "GROUP BY genre " +
            "ORDER BY count DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> getGenreDistribution();

    @Query(value = "SELECT COUNT(*) FROM user_books WHERE user_email = ?1", nativeQuery = true)
    Long getBooksByUser(String userEmail);

    // Transaction Analytics Queries
    @Query(value = "SELECT COUNT(*) FROM transactions WHERE created_at >= ?1", nativeQuery = true)
    Long getMonthlyTransactions(LocalDateTime startDate);

    default Long getMonthlyTransactions() {
        return getMonthlyTransactions(LocalDateTime.now().minusMonths(1));
    }

    @Query(value = "SELECT COUNT(*) FROM transactions WHERE created_at BETWEEN ?1 AND ?2", nativeQuery = true)
    Long getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT COUNT(*) FROM transactions WHERE type IN ('AUCTION', 'DONATION')", nativeQuery = true)
    Long getEventsHosted();

    @Query(value = "SELECT EXTRACT(MONTH FROM t.created_at) as month, COALESCE(SUM(t.payment_amount), 0) as revenue " +
            "FROM transactions t " +
            "WHERE EXTRACT(YEAR FROM t.created_at) = ?1 AND t.payment_status = 'COMPLETED' " +
            "GROUP BY EXTRACT(MONTH FROM t.created_at) " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlyRevenue(int year);

    // User Activity and Trust Score Queries
    @Query(value = "SELECT u.email, u.name, COUNT(t.transaction_id) as transaction_count " +
            "FROM users u " +
            "LEFT JOIN transactions t ON u.user_id = t.borrower_id " +
            "GROUP BY u.user_id, u.email, u.name " +
            "HAVING COUNT(t.transaction_id) > 0 " +
            "ORDER BY transaction_count DESC " +
            "LIMIT ?1", nativeQuery = true)
    List<Object[]> getTopUsersByActivity(int limit);

    @Query(value = "SELECT COUNT(*) FROM transactions t " +
            "JOIN users u ON t.borrower_id = u.user_id " +
            "WHERE u.email = ?1 AND t.status = 'COMPLETED'", nativeQuery = true)
    Long getCompletedTransactionsByUser(String userEmail);

    @Query(value = "SELECT COUNT(*) FROM transactions t " +
            "JOIN users u ON t.borrower_id = u.user_id " +
            "WHERE u.email = ?1", nativeQuery = true)
    Long getTotalTransactionsByUser(String userEmail);

    @Query(value = "SELECT COUNT(*) FROM transactions t " +
            "JOIN users u ON t.borrower_id = u.user_id " +
            "WHERE u.email = ?1 AND t.type = ?2", nativeQuery = true)
    Long getTransactionsByUserAndType(String userEmail, String type);

    @Query(value = "SELECT COUNT(*) FROM transactions t " +
            "JOIN users u ON t.borrower_id = u.user_id " +
            "WHERE u.email = ?1 AND t.created_at < ?2", nativeQuery = true)
    Long getTransactionsByUserBeforeDate(String userEmail, LocalDateTime date);
}