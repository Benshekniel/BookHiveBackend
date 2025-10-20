package service.organization.impl;

import model.dto.organization.FeedbackCreateDTO;
import model.dto.organization.FeedbackDTO;
import model.entity.Donation;
import model.entity.Feedback;
import model.repo.OrgRepo;
import model.repo.organization.DonationRepository;
import model.repo.organization.FeedbackRepository;
import service.organization.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DonationRepository donationRepository;
    private final OrgRepo orgRepo;

    @Autowired
    public FeedbackServiceImpl(
            FeedbackRepository feedbackRepository,
            DonationRepository donationRepository,
            OrgRepo orgRepo) {
        this.feedbackRepository = feedbackRepository;
        this.donationRepository = donationRepository;
        this.orgRepo = orgRepo;
    }

    @Override
    public FeedbackDTO createFeedback(FeedbackCreateDTO feedbackCreateDTO) {
        if (!orgRepo.existsByOrgId(feedbackCreateDTO.getOrganizationId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }

        Donation donation = donationRepository.findById(feedbackCreateDTO.getDonationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not found"));

        if (!donation.getOrganizationId().equals(feedbackCreateDTO.getOrganizationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization ID mismatch");
        }

        if (feedbackRepository.existsByDonationId(feedbackCreateDTO.getDonationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback already exists for this donation");
        }

        if (!"RECEIVED".equals(donation.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Donation must be in RECEIVED state to add feedback");
        }

        if (feedbackCreateDTO.getRating() < 1 || feedbackCreateDTO.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        Feedback feedback = new Feedback();
        feedback.setOrganizationId(feedbackCreateDTO.getOrganizationId());
        feedback.setDonationId(feedbackCreateDTO.getDonationId());
        feedback.setRating(feedbackCreateDTO.getRating());
        feedback.setComment(feedbackCreateDTO.getComment());
        feedback.setDate(LocalDateTime.now());

        Feedback saved = feedbackRepository.save(feedback);
        return mapToDTO(saved);
    }

    @Override
    public List<FeedbackDTO> getFeedbackByOrganization(Long orgId) {
        if (!orgRepo.existsByOrgId(orgId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found");
        }

        List<Feedback> feedbackList = feedbackRepository.findByOrganizationIdOrderByDateDesc(orgId);
        return feedbackList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDTO getFeedbackByDonation(Long donationId) {
        Feedback feedback = feedbackRepository.findByDonationId(donationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found for donation"));
        return mapToDTO(feedback);
    }

    private FeedbackDTO mapToDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setOrganizationId(feedback.getOrganizationId());
        dto.setDonationId(feedback.getDonationId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setDate(feedback.getDate().toString());
        return dto;
    }
}