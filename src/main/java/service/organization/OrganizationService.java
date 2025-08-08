package service.organization;

import model.dto.organization.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrganizationService {

    // Dashboard
    ResponseEntity<OrganizationDashboardDTO> getOrganizationDashboard(Long orgId);

    // Book Requests
    ResponseEntity<String> createBookRequest(Long orgId, BookRequestCreateDTO requestDTO);
    ResponseEntity<List<BookRequestResponseDTO>> getBookRequests(Long orgId, String status);
    ResponseEntity<BookRequestResponseDTO> getBookRequestById(Long orgId, Long requestId);
    ResponseEntity<String> updateBookRequest(Long orgId, Long requestId, BookRequestUpdateDTO updateDTO);
    ResponseEntity<String> deleteBookRequest(Long orgId, Long requestId);

    // Donations
    ResponseEntity<List<DonationReceivedDTO>> getDonationsReceived(Long orgId, String status);
    ResponseEntity<String> confirmDonationReceipt(Long orgId, Long donationId, DonationConfirmationDTO confirmationDTO);

    // Messages
    ResponseEntity<List<MessageDTO>> getMessages(Long orgId);
    ResponseEntity<String> sendMessage(Long orgId, MessageCreateDTO messageDTO);
    ResponseEntity<MessageDTO> getMessageById(Long orgId, Long messageId);
    ResponseEntity<String> markMessageAsRead(Long orgId, Long messageId);

    // Notifications
    ResponseEntity<List<NotificationDTO>> getNotifications(Long orgId);
    ResponseEntity<String> markNotificationAsRead(Long orgId, Long notificationId);

    // Feedback
    ResponseEntity<String> submitFeedback(Long orgId, FeedbackCreateDTO feedbackDTO);
    ResponseEntity<List<FeedbackResponseDTO>> getFeedback(Long orgId);

    // Profile
    ResponseEntity<OrganizationProfileDTO> getOrganizationProfile(Long orgId);
    ResponseEntity<String> updateOrganizationProfile(Long orgId, OrganizationProfileUpdateDTO profileDTO);
}
