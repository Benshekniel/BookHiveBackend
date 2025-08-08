package controller;

import model.dto.organization.*;
import service.organization.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/organization")
@CrossOrigin(origins = "http://localhost:9999")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    // Dashboard - Get organization statistics
    @GetMapping("/{orgId}/dashboard")
    public ResponseEntity<OrganizationDashboardDTO> getDashboard(@PathVariable Long orgId) {
        return organizationService.getOrganizationDashboard(orgId);
    }

    // Book Requests Management
    @PostMapping("/{orgId}/book-requests")
    public ResponseEntity<String> createBookRequest(
            @PathVariable Long orgId,
            @RequestBody BookRequestCreateDTO requestDTO) {
        return organizationService.createBookRequest(orgId, requestDTO);
    }

    @GetMapping("/{orgId}/book-requests")
    public ResponseEntity<List<BookRequestResponseDTO>> getBookRequests(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "all") String status) {
        return organizationService.getBookRequests(orgId, status);
    }

    @GetMapping("/{orgId}/book-requests/{requestId}")
    public ResponseEntity<BookRequestResponseDTO> getBookRequest(
            @PathVariable Long orgId,
            @PathVariable Long requestId) {
        return organizationService.getBookRequestById(orgId, requestId);
    }

    @PutMapping("/{orgId}/book-requests/{requestId}")
    public ResponseEntity<String> updateBookRequest(
            @PathVariable Long orgId,
            @PathVariable Long requestId,
            @RequestBody BookRequestUpdateDTO updateDTO) {
        return organizationService.updateBookRequest(orgId, requestId, updateDTO);
    }

    @DeleteMapping("/{orgId}/book-requests/{requestId}")
    public ResponseEntity<String> deleteBookRequest(
            @PathVariable Long orgId,
            @PathVariable Long requestId) {
        return organizationService.deleteBookRequest(orgId, requestId);
    }

    // Donations Received
    @GetMapping("/{orgId}/donations")
    public ResponseEntity<List<DonationReceivedDTO>> getDonationsReceived(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "all") String status) {
        return organizationService.getDonationsReceived(orgId, status);
    }

    @PostMapping("/{orgId}/donations/{donationId}/confirm-receipt")
    public ResponseEntity<String> confirmDonationReceipt(
            @PathVariable Long orgId,
            @PathVariable Long donationId,
            @RequestBody DonationConfirmationDTO confirmationDTO) {
        return organizationService.confirmDonationReceipt(orgId, donationId, confirmationDTO);
    }

    // Messages
    @GetMapping("/{orgId}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long orgId) {
        return organizationService.getMessages(orgId);
    }

    @PostMapping("/{orgId}/messages")
    public ResponseEntity<String> sendMessage(
            @PathVariable Long orgId,
            @RequestBody MessageCreateDTO messageDTO) {
        return organizationService.sendMessage(orgId, messageDTO);
    }

    @GetMapping("/{orgId}/messages/{messageId}")
    public ResponseEntity<MessageDTO> getMessage(
            @PathVariable Long orgId,
            @PathVariable Long messageId) {
        return organizationService.getMessageById(orgId, messageId);
    }

    @PutMapping("/{orgId}/messages/{messageId}/read")
    public ResponseEntity<String> markMessageAsRead(
            @PathVariable Long orgId,
            @PathVariable Long messageId) {
        return organizationService.markMessageAsRead(orgId, messageId);
    }

    // Notifications
    @GetMapping("/{orgId}/notifications")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long orgId) {
        return organizationService.getNotifications(orgId);
    }

    @PutMapping("/{orgId}/notifications/{notificationId}/read")
    public ResponseEntity<String> markNotificationAsRead(
            @PathVariable Long orgId,
            @PathVariable Long notificationId) {
        return organizationService.markNotificationAsRead(orgId, notificationId);
    }

    // Feedback
    @PostMapping("/{orgId}/feedback")
    public ResponseEntity<String> submitFeedback(
            @PathVariable Long orgId,
            @RequestBody FeedbackCreateDTO feedbackDTO) {
        return organizationService.submitFeedback(orgId, feedbackDTO);
    }

    @GetMapping("/{orgId}/feedback")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedback(@PathVariable Long orgId) {
        return organizationService.getFeedback(orgId);
    }

    // Profile Settings
    @GetMapping("/{orgId}/profile")
    public ResponseEntity<OrganizationProfileDTO> getProfile(@PathVariable Long orgId) {
        return organizationService.getOrganizationProfile(orgId);
    }

    @PutMapping("/{orgId}/profile")
    public ResponseEntity<String> updateProfile(
            @PathVariable Long orgId,
            @RequestBody OrganizationProfileUpdateDTO profileDTO) {
        return organizationService.updateOrganizationProfile(orgId, profileDTO);
    }
}
