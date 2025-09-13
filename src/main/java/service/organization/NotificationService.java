package service.organization;

import model.dto.Organization.NotificationDto.*;
import java.util.List;

public interface NotificationService {
    
    // Get all notifications for an organization
    List<NotificationResponseDto> getNotificationsByOrganization(Long orgId);
    
    // Mark notification as read
    NotificationResponseDto markNotificationAsRead(Long notificationId);
    
    // Mark all notifications as read
    MarkAllReadResponseDto markAllNotificationsAsRead(Long orgId);
    
    // Delete notification
    void deleteNotification(Long notificationId);
}