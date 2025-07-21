package model.repo.Hubmanager;

import model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByHubId(Long hubId);
    List<Delivery> findByAgentId(Long agentId);
    List<Delivery> findByStatus(Delivery.DeliveryStatus status);
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    Long countByHubId(Long hubId);
    Long countByHubIdAndStatus(Long hubId, Delivery.DeliveryStatus status);
    Long countByHubIdAndStatusIn(Long hubId, List<Delivery.DeliveryStatus> statuses);

    Long countByAgentIdAndStatusIn(Long agentId, List<Delivery.DeliveryStatus> statuses);
    Long countByAgentId(Long agentId);
    Long countByAgentIdAndStatus(Long agentId, Delivery.DeliveryStatus status);

    @Query("SELECT d FROM Delivery d WHERE d.createdAt BETWEEN :start AND :end")
    List<Delivery> findByCreatedAtBetween(@Param("start") java.time.LocalDateTime startDate, @Param("end") java.time.LocalDateTime endDate);

    @Query("SELECT d.status, COUNT(d) FROM Delivery d GROUP BY d.status")
    List<Object[]> getDeliveryStatsByStatus();

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.hubId = :hubId")
    Long countTodayDeliveriesByHub(@Param("hubId") Long hubId);

    @Query("SELECT AVG(1.0) FROM Delivery d WHERE d.hubId = :hubId")
    Double getAverageDeliveryTimeByHub(@Param("hubId") Long hubId);

    // Simplified query without problematic fields
    @Query("SELECT d, h.name, au.name, au.email, a.phoneNumber, t.transactionId, b.title, cu.name, cu.email " +
            "FROM Delivery d " +
            "LEFT JOIN Hub h ON d.hubId = h.hubId " +
            "LEFT JOIN Agent a ON d.agentId = a.agentId " +
            "LEFT JOIN AllUsers au ON a.userId = au.user_id " +
            "LEFT JOIN Transaction t ON d.transactionId = t.transactionId " +
            "LEFT JOIN Book b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers cu ON d.userId = cu.user_id")
    List<Object[]> findAllDeliveriesWithAllDetails();
}