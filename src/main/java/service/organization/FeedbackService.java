package service.organization;

import model.dto.organization.FeedbackCreateDTO;
import model.dto.organization.FeedbackResponseDTO;
import java.util.List;

public interface FeedbackService {
    FeedbackResponseDTO createFeedback(FeedbackCreateDTO dto);
    List<FeedbackResponseDTO> getFeedbacksByOrganization(Long orgId);
}
