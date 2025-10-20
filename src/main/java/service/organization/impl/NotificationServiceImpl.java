package service.organization.impl;

import model.dto.organization.NotificationDTO;
import model.entity.Notification;
import model.entity.Organization;
import model.repo.organization.NotificationRepository;
import model.repo.organization.OrganizationRepository;
import service.organization.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            OrganizationRepository organizationRepository) {
        this.notificationRepository = notificationRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<NotificationDTO> getNotificationsByOrganization(Long orgId) {
        // Ensure organization exists
        if (!organizationRepository.existsById(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with id: " + orgId);
        }

        // Get notifications and map to DTOs
        List<Notification> notifications = notificationRepository.findByOrganizationOrgIdOrderByTimestampDesc(orgId);
        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found with id: " + id));

        notification.setRead(true);
        Notification updated = notificationRepository.save(notification);
        return mapToDTO(updated);
    }

    @Override
    public void markAllAsRead(Long orgId) {
        // Ensure organization exists
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found with id: " + orgId));

        // Mark all as read
        notificationRepository.markAllAsReadForOrganization(orgId);
    }

    @Override
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setOrganizationId(notification.getOrganization().getOrgId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setActionUrl(notification.getActionUrl());
        dto.setTimestamp(notification.getTimestamp().toString());
        return dto;
    }
}