// NotificationServiceImpl.java
package service.organization.impl;

import model.dto.Organization.NotificationDto.*;
import model.entity.Notification;
import model.repo.organization.NotificationRepository;
import model.repo.organization.OrganizationRepository;
import service.organization.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public List<NotificationResponseDto> getNotificationsByOrganization(Long orgId) {
        log.info("Fetching notifications for organization ID: {}", orgId);
        
        // Validate organization exists
        if (!organizationRepository.existsById(orgId)) {
            log.warn("Organization not found with ID: {}", orgId);
            return List.of();
        }
        
        return notificationRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId)
                .stream()
                .map(this::convertToResponseDto)
                .toList();
    }

    @Override
    public NotificationResponseDto markNotificationAsRead(Long notificationId) {
        log.info("Marking notification with ID: {} as read", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        log.info("Marked notification with ID: {} as read", notificationId);
        
        return convertToResponseDto(updatedNotification);
    }

    @Override
    public MarkAllReadResponseDto markAllNotificationsAsRead(Long orgId) {
        log.info("Marking all notifications as read for organization ID: {}", orgId);
        
        // Validate organization exists
        if (!organizationRepository.existsById(orgId)) {
            log.warn("Organization not found with ID: {}", orgId);
            return new MarkAllReadResponseDto(0, false, "Organization not found");
        }
        
        int count = notificationRepository.markAllAsReadByOrganizationId(orgId);
        log.info("Marked {} notifications as read for organization ID: {}", count, orgId);
        
        return new MarkAllReadResponseDto(count, true, "Marked " + count + " notifications as read");
    }

    @Override
    public void deleteNotification(Long notificationId) {
        log.info("Deleting notification with ID: {}", notificationId);
        
        if (!notificationRepository.existsById(notificationId)) {
            log.warn("Notification not found with ID: {}", notificationId);
            throw new RuntimeException("Notification not found");
        }
        
        notificationRepository.deleteById(notificationId);
        log.info("Deleted notification with ID: {}", notificationId);
    }
    
    // Helper methods
    private NotificationResponseDto convertToResponseDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setOrganizationId(notification.getOrganizationId());
        dto.setType(notification.getType());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setTitle(notification.getTitle());
        dto.setAction(notification.getAction());
        dto.setReferenceId(notification.getReferenceId());
        dto.setReferenceType(notification.getReferenceType());
        return dto;
    }
}