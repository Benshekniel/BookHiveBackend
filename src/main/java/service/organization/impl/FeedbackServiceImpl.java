package service.organization.impl;

import model.dto.organization.FeedbackCreateDTO;
import model.dto.organization.FeedbackResponseDTO;
import org.springframework.stereotype.Service;
import service.organization.FeedbackService;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Override
    public FeedbackResponseDTO createFeedback(FeedbackCreateDTO dto) {
        // TODO: Map DTO to entity, save, and return response DTO
        return null;
    }

    @Override
    public List<FeedbackResponseDTO> getFeedbacksByOrganization(Long orgId) {
    // TODO: Map entities to DTOs
    return java.util.Collections.emptyList();
    }
}
