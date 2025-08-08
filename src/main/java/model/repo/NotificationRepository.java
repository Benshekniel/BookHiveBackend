package model.repo;

import model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.orgId = :orgId AND n.recipientType = 'ORGANIZATION' " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findByOrgIdOrderByCreatedAtDesc(@Param("orgId") Long orgId);

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.recipientType = 'USER' " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n WHERE n.notificationId = :notificationId AND n.orgId = :orgId AND n.recipientType = 'ORGANIZATION'")
    Notification findByNotificationIdAndOrgId(@Param("notificationId") Long notificationId, @Param("orgId") Long orgId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.orgId = :orgId AND n.recipientType = 'ORGANIZATION' AND n.isRead = false")
    long countUnreadNotificationsByOrgId(@Param("orgId") Long orgId);
}
