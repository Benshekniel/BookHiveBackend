package model.repo;

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

    // Additional methods needed by service
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
}
