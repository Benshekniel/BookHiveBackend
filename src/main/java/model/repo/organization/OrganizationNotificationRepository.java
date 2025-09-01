package model.repo.organization;

import model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrganizationNotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOrganizationId(Long orgId);
}
