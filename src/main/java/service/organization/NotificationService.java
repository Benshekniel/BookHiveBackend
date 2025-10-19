package service.organization;

import model.dto.organization.NotificationDTO;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getNotificationsByOrganization(Long organizationId);

    NotificationDTO markAsRead(Long id);

    void markAllAsRead(Long organizationId);

    void deleteNotification(Long id);
}