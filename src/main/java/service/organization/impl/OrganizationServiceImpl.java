package service.organization.impl;

import model.dto.organization.*;
import model.entity.*;
import model.repo.*;
import service.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private OrganizationFeedbackRepository feedbackRepository;

    @Autowired
    private UsersRepo userRepository;

    @Override
    public ResponseEntity<OrganizationDashboardDTO> getOrganizationDashboard(Long orgId) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Organization org = orgOpt.get();
            
            // Calculate statistics
            long pendingRequests = bookRequestRepository.countByOrganizationAndStatus(org, BookRequest.RequestStatus.PENDING);
            long totalRequests = bookRequestRepository.countByOrganization(org);
            Long booksReceived = donationRepository.getTotalBooksReceivedByOrganization(org);
            long totalDonations = donationRepository.countByRecipientOrganization(org);
            long deliveredDonations = donationRepository.countByRecipientOrganizationAndStatus(org, Donation.DonationStatus.DELIVERED);

            OrganizationDashboardDTO.DashboardStats stats = new OrganizationDashboardDTO.DashboardStats(
                (int) pendingRequests,
                booksReceived != null ? booksReceived.intValue() : 0,
                0, // upcoming events - can be implemented later
                (int) totalDonations,
                (int) totalRequests,
                (int) deliveredDonations
            );

            OrganizationDashboardDTO dashboard = new OrganizationDashboardDTO(
                org.getOrgId(),
                org.getFname() + " " + org.getLname(), // Using contact person name for now
                org.getType(),
                stats
            );

            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> createBookRequest(Long orgId, BookRequestCreateDTO requestDTO) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Organization not found");
            }

            Organization org = orgOpt.get();
            
            BookRequest bookRequest = new BookRequest();
            bookRequest.setOrganization(org);
            bookRequest.setTitle(requestDTO.getTitle());
            bookRequest.setSubject(requestDTO.getSubject());
            bookRequest.setQuantity(requestDTO.getQuantity());
            bookRequest.setDescription(requestDTO.getDescription());
            bookRequest.setUrgency(BookRequest.UrgencyLevel.valueOf(requestDTO.getUrgency().toUpperCase()));
            bookRequest.setCategory(requestDTO.getCategory());
            bookRequest.setGradeLevel(requestDTO.getGradeLevel());
            bookRequest.setNotes(requestDTO.getNotes());
            bookRequest.setStatus(BookRequest.RequestStatus.PENDING);

            bookRequestRepository.save(bookRequest);

            // Create notification for admins/moderators
            createNotificationForAdmins("New Book Request", 
                "New book request submitted by " + org.getFname() + " " + org.getLname(),
                bookRequest.getRequestId(), "request");

            return ResponseEntity.ok("Book request created successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating book request: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<BookRequestResponseDTO>> getBookRequests(Long orgId, String status) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Organization org = orgOpt.get();
            List<BookRequest> requests;

            if ("all".equals(status)) {
                requests = bookRequestRepository.findByOrganizationOrderByDateRequestedDesc(org);
            } else {
                try {
                    BookRequest.RequestStatus requestStatus = BookRequest.RequestStatus.valueOf(status.toUpperCase());
                    requests = bookRequestRepository.findByOrganizationAndStatus(org, requestStatus);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().build();
                }
            }

            List<BookRequestResponseDTO> responseDTOs = requests.stream()
                .map(this::convertToBookRequestResponseDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(responseDTOs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<BookRequestResponseDTO> getBookRequestById(Long orgId, Long requestId) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Organization org = orgOpt.get();
            Optional<BookRequest> requestOpt = bookRequestRepository.findByRequestIdAndOrganization(requestId, org);
            
            if (requestOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            BookRequestResponseDTO responseDTO = convertToBookRequestResponseDTO(requestOpt.get());
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> updateBookRequest(Long orgId, Long requestId, BookRequestUpdateDTO updateDTO) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Organization not found");
            }

            Organization org = orgOpt.get();
            Optional<BookRequest> requestOpt = bookRequestRepository.findByRequestIdAndOrganization(requestId, org);
            
            if (requestOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Book request not found");
            }

            BookRequest bookRequest = requestOpt.get();
            
            // Only allow updates if status is PENDING
            if (bookRequest.getStatus() != BookRequest.RequestStatus.PENDING) {
                return ResponseEntity.badRequest().body("Cannot update request that is not in pending status");
            }

            // Update fields
            if (updateDTO.getTitle() != null) bookRequest.setTitle(updateDTO.getTitle());
            if (updateDTO.getSubject() != null) bookRequest.setSubject(updateDTO.getSubject());
            if (updateDTO.getQuantity() != null) bookRequest.setQuantity(updateDTO.getQuantity());
            if (updateDTO.getDescription() != null) bookRequest.setDescription(updateDTO.getDescription());
            if (updateDTO.getUrgency() != null) bookRequest.setUrgency(BookRequest.UrgencyLevel.valueOf(updateDTO.getUrgency().toUpperCase()));
            if (updateDTO.getCategory() != null) bookRequest.setCategory(updateDTO.getCategory());
            if (updateDTO.getGradeLevel() != null) bookRequest.setGradeLevel(updateDTO.getGradeLevel());
            if (updateDTO.getNotes() != null) bookRequest.setNotes(updateDTO.getNotes());

            bookRequestRepository.save(bookRequest);

            return ResponseEntity.ok("Book request updated successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating book request: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteBookRequest(Long orgId, Long requestId) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Organization not found");
            }

            Organization org = orgOpt.get();
            Optional<BookRequest> requestOpt = bookRequestRepository.findByRequestIdAndOrganization(requestId, org);
            
            if (requestOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Book request not found");
            }

            BookRequest bookRequest = requestOpt.get();
            
            // Only allow deletion if status is PENDING
            if (bookRequest.getStatus() != BookRequest.RequestStatus.PENDING) {
                return ResponseEntity.badRequest().body("Cannot delete request that is not in pending status");
            }

            bookRequestRepository.delete(bookRequest);

            return ResponseEntity.ok("Book request deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting book request: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<DonationReceivedDTO>> getDonationsReceived(Long orgId, String status) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Organization org = orgOpt.get();
            List<Donation> donations;

            if ("all".equals(status)) {
                donations = donationRepository.findByRecipientOrganizationOrderByCreatedAtDesc(org);
            } else {
                try {
                    Donation.DonationStatus donationStatus = Donation.DonationStatus.valueOf(status.toUpperCase());
                    donations = donationRepository.findByRecipientOrganizationAndStatus(org, donationStatus);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().build();
                }
            }

            List<DonationReceivedDTO> responseDTOs = donations.stream()
                .map(this::convertToDonationReceivedDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(responseDTOs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> confirmDonationReceipt(Long orgId, Long donationId, DonationConfirmationDTO confirmationDTO) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Organization not found");
            }

            Optional<Donation> donationOpt = donationRepository.findById(donationId);
            if (donationOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Donation not found");
            }

            Donation donation = donationOpt.get();
            
            // Verify the donation belongs to this organization
            if (!donation.getRecipientOrganization().getOrgId().equals(orgId)) {
                return ResponseEntity.badRequest().body("Donation does not belong to this organization");
            }

            // Update donation with confirmation details
            donation.setStatus(Donation.DonationStatus.RECEIVED);
            donation.setDateReceived(confirmationDTO.getDateReceived() != null ? confirmationDTO.getDateReceived() : LocalDateTime.now());
            donation.setReceivedCondition(confirmationDTO.getReceivedCondition());
            donation.setActualQuantityReceived(confirmationDTO.getActualQuantityReceived());
            donation.setRecipientFeedback(confirmationDTO.getFeedback());
            donation.setAllItemsReceived(confirmationDTO.isAllItemsReceived());

            if (confirmationDTO.getNotes() != null) {
                donation.setNotes(donation.getNotes() + " | Received: " + confirmationDTO.getNotes());
            }

            donationRepository.save(donation);

            // Create notification for donor
            createNotificationForUser(donation.getDonor().getUserId(), 
                "Donation Received", 
                "Your donation has been received by " + orgOpt.get().getFname() + " " + orgOpt.get().getLname(),
                donation.getDonationId(), "donation");

            return ResponseEntity.ok("Donation receipt confirmed successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error confirming donation receipt: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessages(Long orgId) {
        try {
            List<Message> messages = messageRepository.findByReceiverIdAndReceiverTypeOrderBySentAtDesc(
                orgId, Message.ReceiverType.ORGANIZATION);

            List<MessageDTO> messageDTOs = messages.stream()
                .map(this::convertToMessageDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> sendMessage(Long orgId, MessageCreateDTO messageDTO) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Organization not found");
            }

            Message message = new Message();
            message.setSenderId(orgId);
            message.setSenderType(Message.SenderType.ORGANIZATION);
            message.setSubject(messageDTO.getSubject());
            message.setContent(messageDTO.getContent());
            message.setMessageType(Message.MessageType.valueOf(messageDTO.getMessageType().toUpperCase()));
            message.setRelatedEntityId(messageDTO.getRelatedEntityId());

            // Determine receiver based on recipient type
            if ("admin".equals(messageDTO.getRecipientType())) {
                message.setReceiverId(1L); // Assuming admin has ID 1
                message.setReceiverType(Message.ReceiverType.ADMIN);
            } else if ("moderator".equals(messageDTO.getRecipientType())) {
                message.setReceiverId(1L); // Assuming moderator has ID 1
                message.setReceiverType(Message.ReceiverType.MODERATOR);
            } else {
                // Find user by email
                Optional<Users> userOpt = userRepository.findByEmail(messageDTO.getRecipientEmail());
                if (userOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Recipient not found");
                }
                message.setReceiverId(userOpt.get().getUserId());
                message.setReceiverType(Message.ReceiverType.USER);
            }

            messageRepository.save(message);

            return ResponseEntity.ok("Message sent successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<MessageDTO> getMessageById(Long orgId, Long messageId) {
        try {
            Message message = messageRepository.findByMessageIdAndReceiverIdAndReceiverType(
                messageId, orgId, Message.ReceiverType.ORGANIZATION);

            if (message == null) {
                return ResponseEntity.notFound().build();
            }

            MessageDTO messageDTO = convertToMessageDTO(message);
            return ResponseEntity.ok(messageDTO);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> markMessageAsRead(Long orgId, Long messageId) {
        try {
            Message message = messageRepository.findByMessageIdAndReceiverIdAndReceiverType(
                messageId, orgId, Message.ReceiverType.ORGANIZATION);

            if (message == null) {
                return ResponseEntity.status(404).body("Message not found");
            }

            message.setIsRead(true);
            messageRepository.save(message);

            return ResponseEntity.ok("Message marked as read");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error marking message as read: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<NotificationDTO>> getNotifications(Long orgId) {
        try {
            List<Notification> notifications = notificationRepository.findByOrgIdOrderByCreatedAtDesc(orgId);

            List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(this::convertToNotificationDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(notificationDTOs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> markNotificationAsRead(Long orgId, Long notificationId) {
        try {
            Notification notification = notificationRepository.findByNotificationIdAndOrgId(notificationId, orgId);

            if (notification == null) {
                return ResponseEntity.status(404).body("Notification not found");
            }

            notification.setRead(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok("Notification marked as read");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error marking notification as read: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> submitFeedback(Long orgId, FeedbackCreateDTO feedbackDTO) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Organization not found");
            }

            Organization org = orgOpt.get();

            OrganizationFeedback feedback = new OrganizationFeedback();
            feedback.setOrganization(org);
            feedback.setSubject(feedbackDTO.getSubject());
            feedback.setFeedbackType(OrganizationFeedback.FeedbackType.valueOf(feedbackDTO.getFeedbackType().toUpperCase()));
            feedback.setMessage(feedbackDTO.getMessage());
            feedback.setRating(feedbackDTO.getRating());
            feedback.setCategory(OrganizationFeedback.FeedbackCategory.valueOf(feedbackDTO.getCategory().toUpperCase()));
            feedback.setStatus(OrganizationFeedback.FeedbackStatus.SUBMITTED);

            feedbackRepository.save(feedback);

            // Create notification for admins
            createNotificationForAdmins("New Feedback", 
                "New feedback submitted by " + org.getFname() + " " + org.getLname(),
                feedback.getFeedbackId(), "feedback");

            return ResponseEntity.ok("Feedback submitted successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error submitting feedback: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedback(Long orgId) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Organization org = orgOpt.get();
            List<OrganizationFeedback> feedbacks = feedbackRepository.findByOrganizationOrderBySubmittedDateDesc(org);

            List<FeedbackResponseDTO> feedbackDTOs = feedbacks.stream()
                .map(this::convertToFeedbackResponseDTO)
                .collect(Collectors.toList());

            return ResponseEntity.ok(feedbackDTOs);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<OrganizationProfileDTO> getOrganizationProfile(Long orgId) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Organization org = orgOpt.get();
            OrganizationProfileDTO profileDTO = convertToOrganizationProfileDTO(org);

            return ResponseEntity.ok(profileDTO);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<String> updateOrganizationProfile(Long orgId, OrganizationProfileUpdateDTO profileDTO) {
        try {
            Optional<Organization> orgOpt = organizationRepository.findById(orgId);
            if (orgOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Organization not found");
            }

            Organization org = orgOpt.get();

            // Update fields
            if (profileDTO.getContactPersonFirstName() != null) org.setFname(profileDTO.getContactPersonFirstName());
            if (profileDTO.getContactPersonLastName() != null) org.setLname(profileDTO.getContactPersonLastName());
            if (profileDTO.getPhone() != null) org.setPhone(Integer.parseInt(profileDTO.getPhone()));
            if (profileDTO.getAddress() != null) org.setAddress(profileDTO.getAddress());
            if (profileDTO.getCity() != null) org.setCity(profileDTO.getCity());
            if (profileDTO.getState() != null) org.setState(profileDTO.getState());
            if (profileDTO.getZipCode() != null) org.setZip(profileDTO.getZipCode());

            organizationRepository.save(org);

            return ResponseEntity.ok("Profile updated successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating profile: " + e.getMessage());
        }
    }

    // Helper methods for conversions
    private BookRequestResponseDTO convertToBookRequestResponseDTO(BookRequest request) {
        return new BookRequestResponseDTO(
            request.getRequestId(),
            request.getTitle(),
            request.getSubject(),
            request.getQuantity(),
            request.getDescription(),
            request.getStatus().name().toLowerCase(),
            request.getUrgency().name().toLowerCase(),
            request.getCategory(),
            request.getGradeLevel(),
            request.getNotes(),
            request.getDateRequested(),
            request.getDateUpdated(),
            request.getApprovedBy(),
            request.getRejectionReason(),
            request.getTrackingNumber(),
            request.getEstimatedDelivery()
        );
    }

    private DonationReceivedDTO convertToDonationReceivedDTO(Donation donation) {
        String donorName = (donation.getDonor() != null) ? 
            donation.getDonor().getFname() + " " + donation.getDonor().getLname() : "Anonymous";
        
        return new DonationReceivedDTO(
            donation.getDonationId(),
            donation.getBookTitle(),
            donorName,
            donation.getDonorLocation(),
            donation.getQuantity(),
            donation.getStatus().name().toLowerCase(),
            donation.getDateReceived(),
            donation.getDateShipped(),
            donation.getTrackingNumber(),
            donation.getCondition() != null ? donation.getCondition().name().toLowerCase() : null,
            donation.getNotes(),
            donation.getEstimatedDelivery(),
            donation.getBookCategory(),
            donation.getGradeLevel()
        );
    }

    private MessageDTO convertToMessageDTO(Message message) {
        // Get sender name based on sender type
        String senderName = "System";
        String senderEmail = "";
        
        if (message.getSenderType() == Message.SenderType.USER && message.getSenderId() != null) {
            Optional<Users> userOpt = userRepository.findById(message.getSenderId());
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                senderName = user.getFname() + " " + user.getLname();
                senderEmail = user.getEmail();
            }
        } else if (message.getSenderType() == Message.SenderType.ORGANIZATION && message.getSenderId() != null) {
            Optional<Organization> orgOpt = organizationRepository.findById(message.getSenderId());
            if (orgOpt.isPresent()) {
                Organization org = orgOpt.get();
                senderName = org.getFname() + " " + org.getLname();
                senderEmail = org.getEmail();
            }
        }

        return new MessageDTO(
            message.getMessageId(),
            senderName,
            senderEmail,
            message.getSenderType().name().toLowerCase(),
            message.getSubject(),
            message.getContent(),
            message.getIsRead(),
            message.getSentAt(),
            message.getMessageType() != null ? message.getMessageType().name().toLowerCase() : "general",
            message.getRelatedEntityId()
        );
    }

    private NotificationDTO convertToNotificationDTO(Notification notification) {
        return new NotificationDTO(
            notification.getNotificationId(),
            notification.getTitle(),
            notification.getMessage(),
            notification.getType().name().toLowerCase(),
            notification.isRead(),
            notification.getCreatedAt(),
            notification.getActionUrl(),
            notification.getRelatedEntityId(),
            notification.getRelatedEntityType()
        );
    }

    private FeedbackResponseDTO convertToFeedbackResponseDTO(OrganizationFeedback feedback) {
        return new FeedbackResponseDTO(
            feedback.getFeedbackId(),
            feedback.getSubject(),
            feedback.getFeedbackType().name().toLowerCase(),
            feedback.getMessage(),
            feedback.getRating(),
            feedback.getCategory().name().toLowerCase(),
            feedback.getSubmittedDate(),
            feedback.getStatus().name().toLowerCase(),
            feedback.getAdminResponse(),
            feedback.getResponseDate()
        );
    }

    private OrganizationProfileDTO convertToOrganizationProfileDTO(Organization org) {
        return new OrganizationProfileDTO(
            org.getOrgId(),
            org.getFname() + " " + org.getLname(), // Using contact person name as organization name
            org.getType(),
            org.getRegNo(),
            org.getFname(),
            org.getLname(),
            org.getEmail(),
            String.valueOf(org.getPhone()),
            org.getYears(),
            org.getAddress(),
            org.getCity(),
            org.getState(),
            org.getZip(),
            null, // description - can be added to Organization entity
            null, // website - can be added to Organization entity
            org.getImageFileName(),
            "active" // status - can be added to Organization entity
        );
    }

    private void createNotificationForAdmins(String title, String message, Long relatedEntityId, String entityType) {
        // This is a simplified implementation
        // In a real application, you would get all admin IDs and create notifications for each
        Notification notification = new Notification();
        notification.setUserId(1L); // Assuming admin user ID is 1
        notification.setRecipientType(Notification.RecipientType.ADMIN);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.INFO);
        notification.setRelatedEntityId(relatedEntityId);
        notification.setRelatedEntityType(entityType);
        
        notificationRepository.save(notification);
    }

    private void createNotificationForUser(Long userId, String title, String message, Long relatedEntityId, String entityType) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setRecipientType(Notification.RecipientType.USER);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.INFO);
        notification.setRelatedEntityId(relatedEntityId);
        notification.setRelatedEntityType(entityType);
        
        notificationRepository.save(notification);
    }
}
