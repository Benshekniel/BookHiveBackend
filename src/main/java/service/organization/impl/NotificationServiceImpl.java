package service.organization.impl;

import model.dto.organization.NotificationDTO;
import org.springframework.stereotype.Service;
import service.organization.NotificationService;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public List<NotificationDTO> getNotificationsByOrganization(Long orgId) {
    // TODO: Map entities to DTOs
    return java.util.Collections.emptyList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        // TODO: Implement mark as read
    }

    @Override
    public void deleteNotification(Long notificationId) {
        // TODO: Implement delete
    }

    @Override
    public void markAllAsRead(Long orgId) {
        // TODO: Implement mark all as read
    }
}
