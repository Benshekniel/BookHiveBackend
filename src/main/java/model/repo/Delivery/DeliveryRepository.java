package model.repo.Delivery;

import model.entity.Delivery;
import model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // Existing methods
    List<Delivery> findByStatus(Delivery.DeliveryStatus status);
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    List<Delivery> findByRouteId(Long routeId);
    List<Delivery> findByTransactionId(Long transactionId);

    // Add this missing method
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = :status")
    Long countByStatus(@Param("status") Delivery.DeliveryStatus status);

    // Custom queries for hub-related methods (hubId is in Route, not Delivery)
    @Query("SELECT d FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId")
    List<Delivery> findByHubId(@Param("hubId") Long hubId);

    @Query("SELECT COUNT(d) FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId")
    Long countByHubId(@Param("hubId") Long hubId);

    @Query("SELECT COUNT(d) FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId AND d.status = :status")
    Long countByHubIdAndStatus(@Param("hubId") Long hubId, @Param("status") Delivery.DeliveryStatus status);

    @Query("SELECT COUNT(d) FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "WHERE r.hubId = :hubId AND d.status IN :statuses")
    Long countByHubIdAndStatusIn(@Param("hubId") Long hubId, @Param("statuses") List<Delivery.DeliveryStatus> statuses);

    // Custom queries for agent-related methods (agentId is in RouteAssignment, not Delivery)
    @Query("SELECT d FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "LEFT JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "WHERE ra.agentId = :agentId")
    List<Delivery> findByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT COUNT(d) FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "LEFT JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "WHERE ra.agentId = :agentId AND d.status IN :statuses")
    Long countByAgentIdAndStatusIn(@Param("agentId") Long agentId, @Param("statuses") List<Delivery.DeliveryStatus> statuses);

    @Query("SELECT COUNT(d) FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "LEFT JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "WHERE ra.agentId = :agentId")
    Long countByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT COUNT(d) FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "LEFT JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "WHERE ra.agentId = :agentId AND d.status = :status")
    Long countByAgentIdAndStatus(@Param("agentId") Long agentId, @Param("status") Delivery.DeliveryStatus status);

    // New optimized methods for dashboard
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status IN :statuses")
    Long countByStatusIn(@Param("statuses") List<Delivery.DeliveryStatus> statuses);

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.createdAt BETWEEN :startDate AND :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT d.tracking_number, " +
            "COALESCE(cu.name, 'Unknown Customer'), " +
            "COALESCE(au.name, 'Unassigned'), " +
            "d.status, " +
            "d.updated_at " +
            "FROM delivery d " +
            "LEFT JOIN all_users cu ON d.user_id = cu.user_id " +
            "LEFT JOIN route r ON d.route_id = r.route_id " +
            "LEFT JOIN route_assignment ra ON r.route_id = ra.route_id " +
            "LEFT JOIN agent a ON ra.agent_id = a.agent_id " +
            "LEFT JOIN all_users au ON a.user_id = au.user_id " +
            "ORDER BY d.updated_at DESC " +
            "LIMIT ?1", nativeQuery = true)
    List<Object[]> findRecentDeliveriesForDashboard(int limit);

    // Other existing methods
    @Query("SELECT d FROM Delivery d WHERE d.createdAt BETWEEN :start AND :end")
    List<Delivery> findByCreatedAtBetween(@Param("start") LocalDateTime startDate, @Param("end") LocalDateTime endDate);

    @Query("SELECT d.status, COUNT(d) FROM Delivery d GROUP BY d.status")
    List<Object[]> getDeliveryStatsByStatus();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.routeId = :routeId")
    Long countTodayDeliveriesByHub(@Param("routeId") Long routeId);

    @Query("SELECT AVG(1.0) FROM Delivery d WHERE d.routeId = :routeId")
    Double getAverageDeliveryTimeByHub(@Param("routeId") Long routeId);

    @Query("SELECT d, h.name, au.name, au.email, a.phoneNumber, t.transactionId,b.title, cu.name, cu.email " +
            "FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "LEFT JOIN Hub h ON r.hubId = h.hubId " +
            "LEFT JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "LEFT JOIN Agent a ON ra.agentId = a.agentId " +
            "LEFT JOIN AllUsers au ON a.userId = au.user_id " +
            "LEFT JOIN Transaction t ON d.transactionId = t.transactionId " +
            "LEFT JOIN UserBooks b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers cu ON d.userId = cu.user_id")
    List<Object[]> findAllDeliveriesWithAllDetails();

    // Route assignment methods
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = :status")
    List<Route> findActiveRoutesByHub(@Param("hubId") Long hubId, @Param("status") Route.RouteStatus status);

    @Query("SELECT d FROM Delivery d WHERE d.routeId IS NULL")
    List<Delivery> findDeliveriesWithoutRoute();

    @Query("SELECT d FROM Delivery d WHERE d.routeId = :routeId AND d.status = :status")
    List<Delivery> findByRouteIdAndStatus(@Param("routeId") Long routeId, @Param("status") Delivery.DeliveryStatus status);

    // Batch operations for performance
    @Query("SELECT d FROM Delivery d WHERE d.deliveryId IN :deliveryIds")
    List<Delivery> findByDeliveryIds(@Param("deliveryIds") List<Long> deliveryIds);

    // Summary statistics
    @Query("SELECT " +
            "COUNT(d) as totalDeliveries, " +
            "COUNT(CASE WHEN d.status IN ('ASSIGNED', 'PICKED_UP', 'IN_TRANSIT') THEN 1 END) as activeDeliveries, " +
            "COUNT(CASE WHEN d.status = 'DELIVERED' THEN 1 END) as completedDeliveries, " +
            "COUNT(CASE WHEN d.status = 'PENDING' THEN 1 END) as pendingDeliveries, " +
            "COUNT(CASE WHEN d.status = 'FAILED' THEN 1 END) as failedDeliveries " +
            "FROM Delivery d")
    Object[] getDeliverySummaryStats();
}