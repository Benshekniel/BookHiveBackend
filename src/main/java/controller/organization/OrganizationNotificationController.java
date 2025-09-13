// 6. OrganizationNotificationController.java
package controller.organization;

import model.dto.Organization.NotificationDto.*;
import service.organization.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organization-notifications")
@RequiredArgsConstructor
public class OrganizationNotificationController {

    private final NotificationServiceImpl notificationService;

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByOrganization(@PathVariable Long orgId) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByOrganization(orgId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDto> markNotificationAsRead(@PathVariable Long notificationId) {
        try {
            NotificationResponseDto response = notificationService.markNotificationAsRead(notificationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/organization/{orgId}/read-all")
    public ResponseEntity<MarkAllReadResponseDto> markAllNotificationsAsRead(@PathVariable Long orgId) {
        try {
            MarkAllReadResponseDto response = notificationService.markAllNotificationsAsRead(orgId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}