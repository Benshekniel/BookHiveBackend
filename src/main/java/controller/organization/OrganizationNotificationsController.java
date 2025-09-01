package controller.organization;

import model.dto.organization.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.organization.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/organization-notifications")
public class OrganizationNotificationsController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/organization/{orgId}")
    public List<NotificationDTO> getNotificationsByOrganization(@PathVariable Long orgId) {
        return notificationService.getNotificationsByOrganization(orgId);
    }

    @PostMapping("/{notificationId}/mark-read")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @PostMapping("/organization/{orgId}/mark-all-read")
    public void markAllAsRead(@PathVariable Long orgId) {
        notificationService.markAllAsRead(orgId);
    }
}
