package service.organization;

import model.dto.organization.NotificationDTO;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsByOrganization(Long orgId);
    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
    void markAllAsRead(Long orgId);
}
