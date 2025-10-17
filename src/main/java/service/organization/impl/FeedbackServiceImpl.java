package service.organization.impl;

import model.dto.organization.FeedbackDTO;
import model.dto.organization.FeedbackCreateDTO;
import model.entity.Donation;
import model.entity.Feedback;
import model.entity.Organization;
import model.repo.organization.DonationRepository;
import model.repo.organization.FeedbackRepository;
import model.repo.organization.OrganizationRepository;
import service.organization.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final OrganizationRepository organizationRepository;
    private final DonationRepository donationRepository;

    @Autowired
    public FeedbackServiceImpl(
            FeedbackRepository feedbackRepository,
            OrganizationRepository organizationRepository,
            DonationRepository donationRepository) {
        this.feedbackRepository = feedbackRepository;
        this.organizationRepository = organizationRepository;
        this.donationRepository = donationRepository;
    }

    @Override
    public FeedbackDTO createFeedback(FeedbackCreateDTO createDTO) {
        // Find organization
        Organization organization = organizationRepository.findById(createDTO.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Find donation
        Donation donation = donationRepository.findById(createDTO.getDonationId())
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        // Verify donation belongs to organization
        if (!donation.getOrganization().getId().equals(organization.getId())) {
            throw new BadRequestException("Donation does not belong to this organization");
        }

        // Verify donation status is RECEIVED
        if (!"RECEIVED".equals(donation.getStatus())) {
            throw new BadRequestException("Feedback can only be given for received donations");
        }

        // Check if feedback already exists
        if (feedbackRepository.existsByDonationId(donation.getId())) {
            throw new BadRequestException("Feedback has already been submitted for this donation");
        }

        // Create feedback entity
        Feedback feedback = new Feedback();
        feedback.setOrganization(organization);
        feedback.setDonation(donation);
        feedback.setRating(createDTO.getRating());
        feedback.setComment(createDTO.getComment());
        feedback.setDate(LocalDateTime.now());

        // Save and return mapped DTO
        Feedback saved = feedbackRepository.save(feedback);
        return mapToDTO(saved);
    }

    @Override
    public List<FeedbackDTO> getFeedbackByOrganization(Long organizationId) {
        // Ensure organization exists
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization not found");
        }

        // Get feedback and map to DTOs
        List<Feedback> feedbackList = feedbackRepository.findByOrganizationIdOrderByDateDesc(organizationId);
        return feedbackList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDTO getFeedbackByDonation(Long donationId) {
        Feedback feedback = feedbackRepository.findByDonationId(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found for this donation"));
        return mapToDTO(feedback);
    }

    private FeedbackDTO mapToDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setOrganizationId(feedback.getOrganization().getId());
        dto.setDonationId(feedback.getDonation().getId());
        dto.setDonorName(feedback.getDonation().getDonorName());
        dto.setBookTitle(feedback.getDonation().getBookTitle());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setDate(feedback.getDate().toString());
        return dto;
    }
}