package model.repo.Delivery;

import model.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HubRepository extends JpaRepository<Hub, Long> {

    // 🔥 FIXED: Changed "hub" to "hubs" in table name
    @Query(value = "SELECT * FROM hubs ORDER BY hub_id LIMIT :limit", nativeQuery = true)
    List<Hub> findLimitedHubs(@Param("limit") int limit);

    // 🔥 Alternative using Spring Data pagination (recommended)
    @Query(value = "SELECT h FROM Hub h ORDER BY h.hubId")
    List<Hub> findAllOrderedByIdWithLimit(@Param("limit") int limit);

    // Existing methods
    List<Hub> findByCity(String city);

    @Query("SELECT h FROM Hub h WHERE h.name LIKE %:name%")
    List<Hub> findByNameContaining(@Param("name") String name);

    // 🔥 FIXED: Changed "hub" to "hubs" in all native queries
    @Query(value = "SELECT h.hub_id, h.name, h.city, h.address, " +
            "u.name as manager_name, " +
            "COUNT(DISTINCT a.agent_id) as total_agents, " +
            "COUNT(DISTINCT CASE WHEN a.availability_status = 'AVAILABLE' THEN a.agent_id END) as available_agents, " +
            "COUNT(DISTINCT CASE WHEN DATE(d.created_at) = CURRENT_DATE THEN d.delivery_id END) as today_deliveries, " +
            "COUNT(DISTINCT r.route_id) as total_routes, " +
            "COALESCE(SUM(CASE WHEN t.payment_status = 'COMPLETED' THEN t.payment_amount ELSE 0 END), 0) as total_revenue " +
            "FROM hubs h " +
            "LEFT JOIN hub_manager hm ON h.hub_id = hm.hub_id " +
            "LEFT JOIN all_users u ON hm.user_id = u.user_id " +
            "LEFT JOIN agent a ON h.hub_id = a.hub_id " +
            "LEFT JOIN route r ON h.hub_id = r.hub_id " +
            "LEFT JOIN delivery d ON r.route_id = d.route_id " +
            "LEFT JOIN transaction t ON d.transaction_id = t.transaction_id " +
            "GROUP BY h.hub_id, h.name, h.city, h.address, u.name", nativeQuery = true)
    List<Object[]> findHubsSummaryData();

    // 🔥 FIXED: Changed "hub" to "hubs" in native query
    @Query(value = "SELECT h.hub_id, h.name, h.city, h.address, " +
            "u.name as manager_name, " +
            "COUNT(DISTINCT a.agent_id) as total_agents, " +
            "COUNT(DISTINCT CASE WHEN a.availability_status = 'AVAILABLE' THEN a.agent_id END) as available_agents, " +
            "COUNT(DISTINCT CASE WHEN DATE(d.created_at) = CURRENT_DATE THEN d.delivery_id END) as today_deliveries, " +
            "COUNT(DISTINCT r.route_id) as total_routes, " +
            "COALESCE(SUM(CASE WHEN t.payment_status = 'COMPLETED' THEN t.payment_amount ELSE 0 END), 0) as total_revenue " +
            "FROM hubs h " +
            "LEFT JOIN hub_manager hm ON h.hub_id = hm.hub_id " +
            "LEFT JOIN all_users u ON hm.user_id = u.user_id " +
            "LEFT JOIN agent a ON h.hub_id = a.hub_id " +
            "LEFT JOIN route r ON h.hub_id = r.hub_id " +
            "LEFT JOIN delivery d ON r.route_id = d.route_id " +
            "LEFT JOIN transaction t ON d.transaction_id = t.transaction_id " +
            "GROUP BY h.hub_id, h.name, h.city, h.address, u.name " +
            "ORDER BY h.hub_id " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findHubsSummaryDataWithLimit(@Param("limit") int limit);

    // 🔥 FIXED: All other native queries - change "hub" to "hubs"
    @Query(value = "SELECT h.hub_id, h.name, h.city, " +
            "COUNT(DISTINCT a.agent_id) as agent_count, " +
            "COUNT(DISTINCT d.delivery_id) as delivery_count, " +
            "COALESCE(SUM(CASE WHEN t.payment_status = 'COMPLETED' THEN t.payment_amount ELSE 0 END), 0) as revenue, " +
            "COUNT(DISTINCT r.route_id) as route_count " +
            "FROM hubs h " +
            "LEFT JOIN agent a ON h.hub_id = a.hub_id " +
            "LEFT JOIN route r ON h.hub_id = r.hub_id " +
            "LEFT JOIN delivery d ON r.route_id = d.route_id " +
            "LEFT JOIN transaction t ON d.transaction_id = t.transaction_id " +
            "GROUP BY h.hub_id, h.name, h.city " +
            "ORDER BY agent_count DESC " +
            "LIMIT ?1", nativeQuery = true)
    List<Object[]> findTopHubsByAgentCount(int limit);

    @Query(value = "SELECT h.hub_id, h.name, h.city, " +
            "COUNT(DISTINCT a.agent_id) as agent_count, " +
            "COUNT(DISTINCT d.delivery_id) as delivery_count, " +
            "COALESCE(SUM(CASE WHEN t.payment_status = 'COMPLETED' THEN t.payment_amount ELSE 0 END), 0) as revenue, " +
            "COUNT(DISTINCT r.route_id) as route_count " +
            "FROM hubs h " +
            "LEFT JOIN agent a ON h.hub_id = a.hub_id " +
            "LEFT JOIN route r ON h.hub_id = r.hub_id " +
            "LEFT JOIN delivery d ON r.route_id = d.route_id " +
            "LEFT JOIN transaction t ON d.transaction_id = t.transaction_id " +
            "GROUP BY h.hub_id, h.name, h.city " +
            "ORDER BY delivery_count DESC " +
            "LIMIT ?1", nativeQuery = true)
    List<Object[]> findTopHubsByDeliveryCount(int limit);

    @Query(value = "SELECT h.hub_id, h.name, h.city, " +
            "COUNT(DISTINCT a.agent_id) as agent_count, " +
            "COUNT(DISTINCT d.delivery_id) as delivery_count, " +
            "COALESCE(SUM(CASE WHEN t.payment_status = 'COMPLETED' THEN t.payment_amount ELSE 0 END), 0) as revenue, " +
            "COUNT(DISTINCT r.route_id) as route_count " +
            "FROM hubs h " +
            "LEFT JOIN agent a ON h.hub_id = a.hub_id " +
            "LEFT JOIN route r ON h.hub_id = r.hub_id " +
            "LEFT JOIN delivery d ON r.route_id = d.route_id " +
            "LEFT JOIN transaction t ON d.transaction_id = t.transaction_id " +
            "GROUP BY h.hub_id, h.name, h.city " +
            "ORDER BY revenue DESC " +
            "LIMIT ?1", nativeQuery = true)
    List<Object[]> findTopHubsByRevenue(int limit);

    // Additional utility methods for hub management - Using JPQL for simpler queries
    @Query("SELECT COUNT(h) FROM Hub h WHERE h.city = :city")
    Long countHubsByCity(@Param("city") String city);

    @Query("SELECT h FROM Hub h WHERE h.hubManagerId IS NULL")
    List<Hub> findHubsWithoutManager();

    @Query("SELECT h FROM Hub h WHERE h.hubManagerId IS NOT NULL")
    List<Hub> findHubsWithManager();

    // 🔥 FIXED: Performance optimization queries
    @Query(value = "SELECT h.hub_id, h.name, " +
            "COUNT(DISTINCT a.agent_id) as total_agents, " +
            "COUNT(DISTINCT CASE WHEN a.availability_status = 'AVAILABLE' THEN a.agent_id END) as available_agents " +
            "FROM hubs h " +
            "LEFT JOIN agent a ON h.hub_id = a.hub_id " +
            "GROUP BY h.hub_id, h.name", nativeQuery = true)
    List<Object[]> findHubAgentCounts();

    @Query(value = "SELECT h.hub_id, h.name, " +
            "COUNT(DISTINCT d.delivery_id) as total_deliveries, " +
            "COUNT(DISTINCT CASE WHEN DATE(d.created_at) = CURRENT_DATE THEN d.delivery_id END) as today_deliveries " +
            "FROM hubs h " +
            "LEFT JOIN route r ON h.hub_id = r.hub_id " +
            "LEFT JOIN delivery d ON r.route_id = d.route_id " +
            "GROUP BY h.hub_id, h.name", nativeQuery = true)
    List<Object[]> findHubDeliveryCounts();

    // Batch operations for efficiency - Using JPQL
    @Query("SELECT h FROM Hub h WHERE h.hubId IN :hubIds")
    List<Hub> findHubsByIds(@Param("hubIds") List<Long> hubIds);

    // 🔥 FIXED: Statistics for dashboard
    @Query(value = "SELECT " +
            "COUNT(*) as total_hubs, " +
            "COUNT(CASE WHEN h.hub_manager_id IS NOT NULL THEN 1 END) as hubs_with_manager, " +
            "COUNT(CASE WHEN h.hub_manager_id IS NULL THEN 1 END) as hubs_without_manager " +
            "FROM hubs h", nativeQuery = true)
    Object[] getHubStatistics();

    // Simple JPQL queries for basic hub operations
    @Query("SELECT h FROM Hub h ORDER BY h.name")
    List<Hub> findAllOrderByName();

    @Query("SELECT h FROM Hub h WHERE h.city IN :cities")
    List<Hub> findByCities(@Param("cities") List<String> cities);

    // 🔥 FIXED: Hub performance metrics
    @Query(value = "SELECT h.hub_id, h.name, h.city, " +
            "COUNT(DISTINCT a.agent_id) as agents, " +
            "COUNT(DISTINCT CASE WHEN d.created_at >= CURRENT_DATE - INTERVAL '7 days' THEN d.delivery_id END) as weekly_deliveries, " +
            "COUNT(DISTINCT CASE WHEN d.created_at >= CURRENT_DATE - INTERVAL '30 days' THEN d.delivery_id END) as monthly_deliveries, " +
            "COALESCE(AVG(CASE WHEN t.payment_status = 'COMPLETED' THEN t.payment_amount END), 0) as avg_transaction_value " +
            "FROM hubs h " +
            "LEFT JOIN agent a ON h.hub_id = a.hub_id " +
            "LEFT JOIN route r ON h.hub_id = r.hub_id " +
            "LEFT JOIN delivery d ON r.route_id = d.route_id " +
            "LEFT JOIN transaction t ON d.transaction_id = t.transaction_id " +
            "GROUP BY h.hub_id, h.name, h.city " +
            "ORDER BY h.name", nativeQuery = true)
    List<Object[]> findHubPerformanceMetrics();

    // Simplified queries for dashboard that don't require complex date functions
    @Query("SELECT h.hubId, h.name, COUNT(a.agentId) " +
            "FROM Hub h " +
            "LEFT JOIN Agent a ON h.hubId = a.hubId " +
            "GROUP BY h.hubId, h.name " +
            "ORDER BY COUNT(a.agentId) DESC")
    List<Object[]> findHubsByAgentCountSimple();

    @Query("SELECT h.hubId, h.name " +
            "FROM Hub h " +
            "LEFT JOIN Route r ON h.hubId = r.hubId " +
            "LEFT JOIN Delivery d ON r.routeId = d.routeId " +
            "WHERE d.createdAt >= :startDate " +
            "GROUP BY h.hubId, h.name " +
            "ORDER BY COUNT(d.deliveryId) DESC")
    List<Object[]> findHubsByRecentDeliveries(@Param("startDate") LocalDateTime startDate);
}