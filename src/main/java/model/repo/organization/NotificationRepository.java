// NotificationRepository.java
package model.repo.organization;

import model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Basic finder methods
    List<Notification> findByOrganizationIdOrderByCreatedAtDesc(Long organizationId);
    List<Notification> findByOrganizationIdAndReadOrderByCreatedAtDesc(Long organizationId, boolean read);
    List<Notification> findByType(String type);
    
    // Count methods
    int countByOrganizationIdAndReadFalse(Long organizationId);
    int countByType(String type);
    
    // Paginated queries
    Page<Notification> findByOrganizationId(Long organizationId, Pageable pageable);
    Page<Notification> findByOrganizationIdAndRead(Long organizationId, boolean read, Pageable pageable);
    
    // Modifying queries
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :notificationId")
    int markAsRead(@Param("notificationId") Long notificationId);
    
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.organizationId = :orgId AND n.read = false")
    int markAllAsReadByOrganizationId(@Param("orgId") Long organizationId);
    
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :before")
    int deleteOldNotifications(@Param("before") LocalDateTime before);
    
    // Complex queries
    @Query("SELECT n.type, COUNT(n) FROM Notification n " +
           "WHERE n.organizationId = :orgId GROUP BY n.type")
    List<Object[]> getNotificationTypeStatistics(@Param("orgId") Long organizationId);
    
    @Query(value = "INSERT INTO notifications (organization_id, title, message, type, reference_id, reference_type, action, created_at, read) " +
           "VALUES (:orgId, :title, :message, :type, :refId, :refType, :action, NOW(), false) RETURNING id", nativeQuery = true)
    Long createNotification(
            @Param("orgId") Long organizationId, 
            @Param("title") String title,
            @Param("message") String message,
            @Param("type") String type,
            @Param("refId") Long referenceId,
            @Param("refType") String referenceType,
            @Param("action") String action);
    
    // Simpler version matching what's actually used in the service
    @Modifying
    @Query(value = "INSERT INTO notifications (organization_id, title, message, type, reference_id, created_at, read) " +
           "VALUES (:orgId, :title, :message, :type, :refId, NOW(), false)", nativeQuery = true)
    void createNotification(
            @Param("orgId") Long organizationId, 
            @Param("title") String title,
            @Param("message") String message,
            @Param("type") String type,
            @Param("refId") Long referenceId);
}