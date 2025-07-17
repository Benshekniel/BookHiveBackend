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

    // Updated query with correct joins to all_users table
    @Query("SELECT d, h.name as hubName, " +
            "au.name as agentName, au.email as agentEmail, au.phoneNumber as agentPhone, " +
            "CAST(t.transactionId AS long) as transactionId, b.title as bookTitle, b.author as bookAuthor, " +
            "cu.name as customerName, cu.email as customerEmail, cu.phoneNumber as customerPhone " +
            "FROM Delivery d " +
            "LEFT JOIN Hub h ON d.hubId = h.hubId " +
            "LEFT JOIN AllUsers au ON d.agentId = au.user_id " +
            "LEFT JOIN Transaction t ON d.transactionId = t.transactionId " +
            "LEFT JOIN Book b ON t.bookId = b.bookId " +
            "LEFT JOIN AllUsers cu ON d.userId = cu.user_id")
    List<Object[]> findAllDeliveriesWithAllDetails();


//    @Query("SELECT d, h.name as hubName, " +
//            "au.name as agentName, au.email as agentEmail, au.phoneNumber as agentPhone, " +
//            "t.transactionId as transactionId, b.title as bookTitle, b.author as bookAuthor, " +
//            "cu.name as customerName, cu.email as customerEmail, cu.phoneNumber as customerPhone " +
//            "FROM Delivery d " +
//            "LEFT JOIN Hub h ON d.hubId = h.hubId " +
//            "LEFT JOIN AllUsers au ON d.agentId = au.userId " +
//            "LEFT JOIN Transaction t ON d.transactionId = t.transactionId " +
//            "LEFT JOIN Book b ON t.bookId = b.bookId " +
//            "LEFT JOIN AllUsers cu ON d.userId = cu.userId " +
//            "WHERE d.deliveryId = :deliveryId")
//    Optional<Object[]> findDeliveryWithAllDetails(@Param("deliveryId") Long deliveryId);
}