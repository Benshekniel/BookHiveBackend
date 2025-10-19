package model.repo.organization;

import model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOrganizationOrgIdOrderByTimestampDesc(Long organizationId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.read = true WHERE n.organization.orgId = :organizationId AND n.read = false")
    void markAllAsReadForOrganization(@Param("organizationId") Long organizationId);
}