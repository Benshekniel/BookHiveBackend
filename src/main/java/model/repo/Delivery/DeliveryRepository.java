package model.repo.Delivery;

import model.entity.Delivery;
import model.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // Direct methods that work (these fields exist in Delivery entity)
    List<Delivery> findByStatus(Delivery.DeliveryStatus status);
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    List<Delivery> findByRouteId(Long routeId);

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

    // Other custom queries
    @Query("SELECT d FROM Delivery d WHERE d.createdAt BETWEEN :start AND :end")
    List<Delivery> findByCreatedAtBetween(@Param("start") java.time.LocalDateTime startDate, @Param("end") java.time.LocalDateTime endDate);

    @Query("SELECT d.status, COUNT(d) FROM Delivery d GROUP BY d.status")
    List<Object[]> getDeliveryStatsByStatus();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.routeId = :routeId")
    Long countTodayDeliveriesByHub(@Param("routeId") Long routeId);

    @Query("SELECT AVG(1.0) FROM Delivery d WHERE d.routeId = :routeId")
    Double getAverageDeliveryTimeByHub(@Param("routeId") Long routeId);

    @Query("SELECT d, h.name, au.name, au.email, a.phoneNumber, t.transactionId, b.title, cu.name, cu.email " +
            "FROM Delivery d " +
            "LEFT JOIN Route r ON d.routeId = r.routeId " +
            "LEFT JOIN Hub h ON r.hubId = h.hubId " +
            "LEFT JOIN RouteAssignment ra ON r.routeId = ra.routeId " +
            "LEFT JOIN Agent a ON ra.agentId = a.agentId " +
            "LEFT JOIN AllUsers au ON a.userId = au.user_id " +
            "LEFT JOIN Transaction t ON d.transactionId = t.transactionId " +
            "LEFT JOIN Book b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers cu ON d.userId = cu.user_id")
    List<Object[]> findAllDeliveriesWithAllDetails();

    // New methods for route assignment
    @Query("SELECT r FROM Route r WHERE r.hubId = :hubId AND r.status = :status")
    List<Route> findActiveRoutesByHub(@Param("hubId") Long hubId, @Param("status") Route.RouteStatus status);

    // Find deliveries without assigned routes
    @Query("SELECT d FROM Delivery d WHERE d.routeId IS NULL")
    List<Delivery> findDeliveriesWithoutRoute();

    // Find deliveries by route and status
    @Query("SELECT d FROM Delivery d WHERE d.routeId = :routeId AND d.status = :status")
    List<Delivery> findByRouteIdAndStatus(@Param("routeId") Long routeId, @Param("status") Delivery.DeliveryStatus status);

    // Update route assignment for a delivery
    @Query("UPDATE Delivery d SET d.routeId = :routeId WHERE d.deliveryId = :deliveryId")
    int updateRouteAssignment(@Param("deliveryId") Long deliveryId, @Param("routeId") Long routeId);
}