package service.organization;

import model.dto.organization.FeedbackDTO;
import model.dto.organization.FeedbackCreateDTO;

import java.util.List;

public interface FeedbackService {

    FeedbackDTO createFeedback(FeedbackCreateDTO createDTO);

    List<FeedbackDTO> getFeedbackByOrganization(Long organizationId);

    FeedbackDTO getFeedbackByDonation(Long donationId);
}