// FeedbackServiceImpl.java
package service.organization.impl;

import model.dto.Organization.FeedbackDto.*;
import model.entity.Feedback;
import model.repo.organization.FeedbackRepository;
import model.repo.organization.DonationRepository;
import model.repo.organization.OrganizationRepository;
import model.repo.organization.NotificationRepository;
import service.organization.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DonationRepository donationRepository;
    private final OrganizationRepository organizationRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public FeedbackResponseDto createFeedback(FeedbackCreateDto feedbackData) {
        log.info("Creating feedback for organization ID: {}", feedbackData.getOrganizationId());
        
        // Validate organization exists
        organizationRepository.findById(feedbackData.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        
        // If donation ID is provided, validate it exists
        if (feedbackData.getDonationId() != null) {
            donationRepository.findById(feedbackData.getDonationId())
                    .orElseThrow(() -> new RuntimeException("Donation not found"));
        }
        
        // Create feedback
        Feedback feedback = new Feedback();
        feedback.setOrganizationId(feedbackData.getOrganizationId());
        feedback.setDonationId(feedbackData.getDonationId());
        feedback.setRating(feedbackData.getRating());
        feedback.setComment(feedbackData.getComment());
        feedback.setCreatedAt(LocalDateTime.now());
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Created feedback with ID: {}", savedFeedback.getId());
        
        // Create notification for organization
        createNotification(
            feedbackData.getOrganizationId(),
            "New feedback received",
            "You have received a new feedback with rating: " + feedbackData.getRating(),
            "FEEDBACK_RECEIVED",
            savedFeedback.getId()
        );
        
        return convertToResponseDto(savedFeedback);
    }

    @Override
    public List<FeedbackResponseDto> getFeedbackByOrganization(Long orgId) {
        log.info("Fetching feedback for organization ID: {}", orgId);
        
        return feedbackRepository.findByOrganizationIdOrderByCreatedAtDesc(orgId)
                .stream()
                .map(this::convertToResponseDto)
                .toList();
    }
    
    // Helper methods
    private FeedbackResponseDto convertToResponseDto(Feedback feedback) {
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId(feedback.getId());
        dto.setOrganizationId(feedback.getOrganizationId());
        dto.setDonationId(feedback.getDonationId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedAt(feedback.getCreatedAt());
        
        // Get organization name
        organizationRepository.findById(feedback.getOrganizationId())
                .ifPresent(org -> dto.setOrganizationName(org.getName()));
        
        // Get donation details if applicable
        if (feedback.getDonationId() != null) {
            donationRepository.findById(feedback.getDonationId())
                    .ifPresent(donation -> {
                        dto.setDonationTitle(donation.getBookTitle());
                        dto.setDonorName(donation.getDonorName());
                    });
        }
        
        return dto;
    }
    
    private void createNotification(Long organizationId, String title, String message, String type, Long referenceId) {
        try {
            // This is a placeholder implementation - replace with your actual notification logic
            if (notificationRepository != null) {
                notificationRepository.createNotification(organizationId, title, message, type, referenceId);
            }
        } catch (Exception e) {
            log.error("Failed to create notification", e);
        }
    }
}