package service.organization;

import model.dto.Organization.FeedbackDto.*;
import java.util.List;

public interface FeedbackService {
    
    // Create feedback
    FeedbackResponseDto createFeedback(FeedbackCreateDto feedbackData);
    
    // Get all feedback for an organization
    List<FeedbackResponseDto> getFeedbackByOrganization(Long orgId);
}