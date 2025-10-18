package controller.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import model.dto.organization.NotificationDTO;
import service.organization.NotificationService;

import java.util.List;
@CrossOrigin(origins = {"http://localhost:9999", "http://localhost:3000"})

@RestController
@RequestMapping("/api/organization-notifications")
public class OrganizationNotificationController {

    private final NotificationService notificationService;

    @Autowired
    public OrganizationNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByOrganization(@PathVariable Long organizationId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByOrganization(organizationId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        NotificationDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/organization/{organizationId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long organizationId) {
        notificationService.markAllAsRead(organizationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}